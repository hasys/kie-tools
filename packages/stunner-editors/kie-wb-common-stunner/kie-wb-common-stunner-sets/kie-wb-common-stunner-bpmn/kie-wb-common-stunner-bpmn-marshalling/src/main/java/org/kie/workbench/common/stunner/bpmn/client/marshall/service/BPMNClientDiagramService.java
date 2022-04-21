/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.client.marshall.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import elemental2.promise.Promise;
import org.kie.workbench.common.stunner.bpmn.client.workitem.WorkItemDefinitionClientService;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagram;
import org.kie.workbench.common.stunner.bpmn.definition.BpmnContainer;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseConnector;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.FlowNode;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.FlowNodeRef;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Lane;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.NonDirectionalAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Process;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.SequenceFlow;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmndi.BpmnDiagram;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmndi.BpmnEdge;
import org.kie.workbench.common.stunner.bpmn.definition.models.di.Waypoint;
import org.kie.workbench.common.stunner.bpmn.factory.BPMNDiagramFactory;
import org.kie.workbench.common.stunner.bpmn.workitem.WorkItemDefinition;
import org.kie.workbench.common.stunner.core.api.DefinitionManager;
import org.kie.workbench.common.stunner.core.api.FactoryManager;
import org.kie.workbench.common.stunner.core.client.api.ShapeManager;
import org.kie.workbench.common.stunner.core.client.service.ClientRuntimeError;
import org.kie.workbench.common.stunner.core.client.service.ServiceCallback;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.diagram.DiagramImpl;
import org.kie.workbench.common.stunner.core.diagram.DiagramParsingException;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.kie.workbench.common.stunner.core.diagram.MetadataImpl;
import org.kie.workbench.common.stunner.core.factory.impl.EdgeFactoryImpl;
import org.kie.workbench.common.stunner.core.factory.impl.NodeFactoryImpl;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Graph;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.definition.Definition;
import org.kie.workbench.common.stunner.core.graph.content.view.ControlPoint;
import org.kie.workbench.common.stunner.core.graph.content.view.MagnetConnection;
import org.kie.workbench.common.stunner.core.graph.content.view.ViewConnector;
import org.kie.workbench.common.stunner.core.graph.util.GraphUtils;
import org.kie.workbench.common.stunner.core.util.DefinitionUtils;
import org.kie.workbench.common.stunner.kogito.client.service.AbstractKogitoClientDiagramService;
import org.uberfire.client.promise.Promises;

import static org.kie.workbench.common.stunner.bpmn.util.XmlUtils.createValidId;

@ApplicationScoped
public class BPMNClientDiagramService extends AbstractKogitoClientDiagramService {

    static final String DEFAULT_PACKAGE = "com.example";
    static final String NO_DIAGRAM_MESSAGE = "No BPMN Diagram can be found.";

    private final DefinitionManager definitionManager;
    private final BPMNClientMarshalling marshalling;
    private final FactoryManager factoryManager;
    private final BPMNDiagramFactory diagramFactory;
    private final ShapeManager shapeManager;
    private final Promises promises;
    private final WorkItemDefinitionClientService widService;
    private final NodeFactoryImpl nodeFactory;
    private final EdgeFactoryImpl edgeFactory;
    private final DefinitionUtils definitionUtils;

    //CDI proxy
    protected BPMNClientDiagramService() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    @Inject
    public BPMNClientDiagramService(final DefinitionManager definitionManager,
                                    final BPMNClientMarshalling marshalling,
                                    final FactoryManager factoryManager,
                                    final BPMNDiagramFactory diagramFactory,
                                    final ShapeManager shapeManager,
                                    final Promises promises,
                                    final WorkItemDefinitionClientService widService,
                                    final NodeFactoryImpl nodeFactory,
                                    final EdgeFactoryImpl edgeFactory,
                                    final DefinitionUtils definitionUtils) {
        this.definitionManager = definitionManager;
        this.marshalling = marshalling;
        this.factoryManager = factoryManager;
        this.diagramFactory = diagramFactory;
        this.shapeManager = shapeManager;
        this.promises = promises;
        this.widService = widService;
        this.nodeFactory = nodeFactory;
        this.edgeFactory = edgeFactory;
        this.definitionUtils = definitionUtils;
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public void transform(final String xml,
                          final ServiceCallback<Diagram> callback) {
        doTransform(DEFAULT_DIAGRAM_ID, xml, callback);
    }

    @Override
    @SuppressWarnings({"rawtypes"})
    public void transform(final String fileName,
                          final String xml,
                          final ServiceCallback<Diagram> callback) {
        doTransform(createDiagramTitleFromFilePath(fileName), xml, callback);
    }

    @SuppressWarnings({"rawtypes"})
    private void doTransform(final String fileName,
                             final String xml,
                             final ServiceCallback<Diagram> callback) {
        final Metadata metadata = createMetadata();
        widService
                .call(metadata)
                .then(wid -> {
                    Diagram diagram = doTransform(fileName, xml);
                    callback.onSuccess(diagram);
                    return promises.resolve();
                })
                .catch_((Promise.CatchOnRejectedCallbackFn<Collection<WorkItemDefinition>>) error -> {
                    callback.onError(new ClientRuntimeError(new DiagramParsingException(metadata, xml)));
                    return promises.resolve();
                });
    }

    @SuppressWarnings({"rawtypes"})
    private Diagram doTransform(final String fileName,
                                final String xml) {

        if (Objects.isNull(xml) || xml.isEmpty()) {
            return createNewDiagram(fileName);
        }
        return parse(fileName, xml);
    }

    @SuppressWarnings({"rawtypes"})
    public Promise<String> transform(final Diagram diagram) {
        String raw = marshalling.marshall(convert(diagram));
        return promises.resolve(raw);
    }

    @SuppressWarnings({"rawtypes"})
    private void updateDiagramSet(Node<Definition<Object>, ?> diagramNode, String name) {
        final BPMNDiagram diagramSet = (BPMNDiagram) diagramNode.getContent().getDefinition();

        if (diagramSet.getPackageName() == null ||
                diagramSet.getName().isEmpty()) {
            diagramSet.setName(name);
        }

        if (diagramSet.getPackageName() == null ||
                diagramSet.getId().isEmpty()) {
            diagramSet.setId(createValidId(name));
        }

        if (diagramSet.getPackageName() == null ||
                diagramSet.getPackageName().isEmpty()) {
            diagramSet.setPackageName(DEFAULT_PACKAGE);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Diagram createNewDiagram(String fileName) {
        final String title = createDiagramTitleFromFilePath(fileName);
        final String defSetId = BPMNClientMarshalling.getDefinitionSetId();
        final Metadata metadata = createMetadata();
        metadata.setTitle(title);
        final Diagram diagram = factoryManager.newDiagram(title,
                                                          defSetId,
                                                          metadata);

        final Node<Definition<Object>, ?> diagramNode = GraphUtils.getFirstNode((Graph<?, Node>) diagram.getGraph(), Process.class);

        updateDiagramSet(diagramNode, fileName);
        updateClientMetadata(diagram);
        return diagram;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Diagram parse(final String fileName, final String raw) {
        final Metadata metadata = createMetadata();
        final String title = createDiagramTitleFromFilePath(fileName);
        final String defSetId = BPMNClientMarshalling.getDefinitionSetId();
        metadata.setTitle(title);

        final Definitions definitions = marshalling.unmarshall(raw);
        final Diagram diagram = factoryManager.newDiagram(title,
                                                          defSetId,
                                                          metadata);

        final Node<Definition<Object>, ?> diagramNode = GraphUtils.getFirstNode((Graph<?, Node>) diagram.getGraph(), Process.class);

        diagramNode.getContent().setDefinition(definitions.getProcess());

        NodeMarshallerFactory factory = new NodeMarshallerFactory(nodeFactory, definitionUtils, definitions);
        Graph graph = diagram.getGraph();

        Map<String, Node<Definition<Object>, Edge>> nodesParents = buildLanes(factory, definitions.getProcess().getLanes(), graph, diagramNode);
        BpmnContainer processContainer = definitions.getProcess();
        BpmnDiagram bpmnDiagram = definitions.getBpmnDiagram();

        buildContainer(processContainer, bpmnDiagram, diagram, diagramNode, factory, graph, nodesParents);

        updateDiagramSet(diagramNode, fileName);
        updateClientMetadata(diagram);

        return diagram;
    }

    private void buildContainer(BpmnContainer processContainer, BpmnDiagram bpmnDiagram, Diagram diagram, Node<Definition<Object>, ?> diagramNode, NodeMarshallerFactory factory, Graph graph, Map<String, Node<Definition<Object>, Edge>> nodesParents) {
        addNodesToGraph(factory, processContainer.getStartEvents(), graph, diagramNode, nodesParents);
        addNodesToGraph(factory, processContainer.getEndEvents(), graph, diagramNode, nodesParents);
        addNodesToGraph(factory, processContainer.getIntermediateCatchEvent(), graph, diagramNode, nodesParents);
        addNodesToGraph(factory, processContainer.getIntermediateThrowEvent(), graph, diagramNode, nodesParents);
        addNodesToGraph(factory, processContainer.getTextAnnotations(), graph, diagramNode, nodesParents);
        addNodesToGraph(factory, processContainer.getDataObjectsReference(), graph, diagramNode, nodesParents);
        addGatewaysToGraph(processContainer, factory, graph, diagramNode, nodesParents);
        addTasksToGraph(processContainer, factory, graph, diagramNode, nodesParents);
        buildConnections(processContainer, bpmnDiagram, diagram);
        addSubprocessesToGraph(factory, processContainer.getSubProcesses(), graph, diagramNode, nodesParents, bpmnDiagram, diagram);
        addSubprocessesToGraph(factory, processContainer.getAdHocSubProcess(), graph, diagramNode, nodesParents, bpmnDiagram, diagram);
    }

    @SuppressWarnings({"rawtypes"})
    private void buildConnections(BpmnContainer processContainer, BpmnDiagram bpmnDiagram, Diagram diagram) {
        for (SequenceFlow sequenceFlow : processContainer.getSequenceFlows()) {
            buildConnection(bpmnDiagram, diagram, sequenceFlow);
        }
        for (NonDirectionalAssociation association : processContainer.getAssociations()) {
            buildConnection(bpmnDiagram, diagram, association);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void buildConnection(BpmnDiagram bpmnDiagram, Diagram diagram, BaseConnector sequenceFlow) {
        for (BpmnEdge bpmnEdge : bpmnDiagram.getBpmnPlane().getBpmnEdges()) {
            if (bpmnEdge.getBpmnElement().equals(sequenceFlow.getId())) {
                final String sourceRef = sequenceFlow.getSourceRef();
                final String targetRef = sequenceFlow.getTargetRef();

                double sourceWaypointX = bpmnEdge.getWaypoint().get(0).getX();
                double sourceWaypointY = bpmnEdge.getWaypoint().get(0).getY();

                double targetWaypointX = bpmnEdge.getWaypoint().get(bpmnEdge.getWaypoint().size() - 1).getX();
                double targetWaypointY = bpmnEdge.getWaypoint().get(bpmnEdge.getWaypoint().size() - 1).getY();

                final Edge<Definition<Object>, Node> build = edgeFactory.build(sequenceFlow.getId(), sequenceFlow);

                build.setSourceNode(diagram.getGraph().getNode(sourceRef));
                build.setTargetNode(diagram.getGraph().getNode(targetRef));

                MagnetConnection sourceConnection = MagnetConnection.Builder.at(sourceWaypointX, sourceWaypointY)
                        .setAuto(sequenceFlow.isAutoConnectionSource());
                MagnetConnection targetConnection = MagnetConnection.Builder.at(targetWaypointX, targetWaypointY)
                        .setAuto(sequenceFlow.isAutoConnectionTarget());

                ViewConnector<Object> connector = (ViewConnector<Object>) build.getContent();

                if (bpmnEdge.getWaypoint().size() > 2) {
                    final List<Waypoint> waypoints = bpmnEdge.getWaypoint().subList(1, bpmnEdge.getWaypoint().size() - 1);
                    ControlPoint[] controlPoints = new ControlPoint[waypoints.size()];

                    for (int i = 0; i < waypoints.size(); i++) {
                        controlPoints[i] = ControlPoint.build(waypoints.get(i).getX(), waypoints.get(i).getY());
                    }

                    connector.setControlPoints(controlPoints);
                }

                connector.setSourceConnection(sourceConnection);
                connector.setTargetConnection(targetConnection);

                diagram.getGraph().getNode(sourceRef).getOutEdges().add(build);
                diagram.getGraph().getNode(targetRef).getInEdges().add(build);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map<String, Node<Definition<Object>, Edge>> buildLanes(NodeMarshallerFactory factory, List<Lane> lanes, Graph graph, Node<Definition<Object>, ?> parent) {
        Map<String, Node<Definition<Object>, Edge>> parentForTheNode = new HashMap<>();
        for (Lane lane : lanes) {
            Node<Definition<Object>, Edge> graphNode = factory.buildNode(lane.getId(), lane, parent);
            if (graphNode != null) {
                graph.addNode(graphNode);
            }

            for (FlowNodeRef nodeRef : lane.getFlowNodeRefs()) {
                parentForTheNode.put(nodeRef.getValue(), graphNode);
            }
        }

        return parentForTheNode;
    }

    private void addGatewaysToGraph(BpmnContainer processContainer, NodeMarshallerFactory factory, Graph graph, Node<Definition<Object>, ?> parent, Map<String, Node<Definition<Object>, Edge>> parents) {
        addNodesToGraph(factory, processContainer.getParallelGateways(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getExclusiveGateways(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getInclusiveGateways(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getEventBasedGateways(), graph, parent, parents);
    }

    private void addTasksToGraph(BpmnContainer processContainer, NodeMarshallerFactory factory, Graph graph, Node<Definition<Object>, ?> parent, Map<String, Node<Definition<Object>, Edge>> parents) {
        addNodesToGraph(factory, processContainer.getTasks(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getScriptTasks(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getUserTasks(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getGenericServiceTask(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getBusinessRuleTask(), graph, parent, parents);
        addNodesToGraph(factory, processContainer.getCallActivities(), graph, parent, parents);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addNodesToGraph(NodeMarshallerFactory factory, List<? extends FlowNode> nodes, Graph graph, Node<Definition<Object>, ?> parent, Map<String, Node<Definition<Object>, Edge>> parents) {
        for (FlowNode flowNode : nodes) {
            Node<Definition<Object>, Edge> graphNode = buildGraphNode(factory, parent, parents, flowNode);
            if (graphNode != null) {
                graph.addNode(graphNode);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addSubprocessesToGraph(NodeMarshallerFactory factory, List<? extends FlowNode> subProcesses, Graph graph, Node<Definition<Object>, ?> parent, Map<String, Node<Definition<Object>, Edge>> parents, BpmnDiagram bpmnDiagram, Diagram diagram) {
        for (FlowNode subProcess : subProcesses) {
            Node<Definition<Object>, Edge> graphNode = buildGraphNode(factory, parent, parents, subProcess);
            if (graphNode != null) {
                graph.addNode(graphNode);
            }

            buildContainer((BpmnContainer) subProcess, bpmnDiagram, diagram, graphNode, factory, graph, parents);
        }
    }

    @SuppressWarnings({"rawtypes"})
    private Node<Definition<Object>, Edge> buildGraphNode(NodeMarshallerFactory factory, Node<Definition<Object>, ?> parent, Map<String, Node<Definition<Object>, Edge>> parents, FlowNode flowNode) {
        Node<Definition<Object>, ?> p = parents.get(flowNode.getId()) != null ? parents.get(flowNode.getId()) : parent;
        return factory.buildNode(flowNode, p);
    }

    private Metadata createMetadata() {
        return new MetadataImpl.MetadataImplBuilder(BPMNClientMarshalling.getDefinitionSetId(),
                                                    definitionManager)
                .build();
    }

    @SuppressWarnings({"rawtypes"})
    private void updateClientMetadata(final Diagram diagram) {
        if (null != diagram) {
            final Metadata metadata = diagram.getMetadata();
            if (Objects.nonNull(metadata)) {
                final String sId = shapeManager.getDefaultShapeSet(metadata.getDefinitionSetId()).getId();
                metadata.setShapeSetId(sId);
            }
        }
    }

    @SuppressWarnings({"rawtypes"})
    private DiagramImpl convert(final Diagram diagram) {
        return new DiagramImpl(diagram.getName(),
                               diagram.getGraph(),
                               diagram.getMetadata());
    }
}
