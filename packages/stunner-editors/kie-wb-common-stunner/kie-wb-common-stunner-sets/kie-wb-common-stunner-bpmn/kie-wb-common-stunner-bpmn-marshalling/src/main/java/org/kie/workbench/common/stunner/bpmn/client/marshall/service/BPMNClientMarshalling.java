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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.stream.XMLStreamException;

import com.google.gwt.core.client.GWT;
import org.kie.workbench.common.stunner.bpmn.BPMNDefinitionSet;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNViewDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.BpmnContainer;
import org.kie.workbench.common.stunner.bpmn.definition.FlowElement;
import org.kie.workbench.common.stunner.bpmn.definition.HasIncoming;
import org.kie.workbench.common.stunner.bpmn.definition.HasOutgoing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Association;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseIntermediateEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BusinessRuleTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.CustomTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataObjectReference;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Definitions_XMLMapperImpl;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EmbeddedSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndMessageEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndSignalEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.ExtensionElements;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.FlowNodeRef;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.GenericServiceTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Import;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Incoming;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateErrorEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateEscalationEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateMessageEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateMessageEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateSignalEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateSignalEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.ItemDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Lane;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.NonDirectionalAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Outgoing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Process;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Property;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Relationship;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.SequenceFlow;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Signal;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartErrorEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartMessageEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartSignalEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.TextAnnotation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.UserTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmndi.BpmnDiagram;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmndi.BpmnEdge;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmndi.BpmnPlane;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmndi.BpmnShape;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpsim.BPSimData;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpsim.ElementParameters;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpsim.Scenario;
import org.kie.workbench.common.stunner.bpmn.definition.models.dc.Bounds;
import org.kie.workbench.common.stunner.bpmn.definition.models.di.Waypoint;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.imports.ImportsValue;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.imports.WSDLImport;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.compensation.HasActivityRef;
import org.kie.workbench.common.stunner.core.definition.adapter.binding.BindableAdapterUtils;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.kie.workbench.common.stunner.core.graph.Graph;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.definition.DefinitionSet;
import org.kie.workbench.common.stunner.core.graph.content.relationship.Child;
import org.kie.workbench.common.stunner.core.graph.content.view.Connection;
import org.kie.workbench.common.stunner.core.graph.content.view.ControlPoint;
import org.kie.workbench.common.stunner.core.graph.content.view.DiscreteConnection;
import org.kie.workbench.common.stunner.core.graph.content.view.Point2D;
import org.kie.workbench.common.stunner.core.graph.content.view.View;
import org.kie.workbench.common.stunner.core.graph.content.view.ViewConnector;
import org.kie.workbench.common.stunner.core.graph.content.view.ViewConnectorImpl;
import org.kie.workbench.common.stunner.core.graph.content.view.ViewImpl;
import org.kie.workbench.common.stunner.core.graph.impl.NodeImpl;
import org.kie.workbench.common.stunner.core.graph.processing.traverse.content.ChildrenTraverseCallback;
import org.kie.workbench.common.stunner.core.graph.processing.traverse.content.ChildrenTraverseProcessorImpl;
import org.kie.workbench.common.stunner.core.graph.processing.traverse.tree.TreeWalkTraverseProcessorImpl;
import org.kie.workbench.common.stunner.core.graph.util.GraphUtils;

import static java.util.stream.StreamSupport.stream;

@ApplicationScoped
public class BPMNClientMarshalling {

    private static Definitions_XMLMapperImpl mapper = Definitions_XMLMapperImpl.INSTANCE;

    @Inject
    public BPMNClientMarshalling() {
    }

    @PostConstruct
    public void init() {
    }

    @SuppressWarnings("unchecked")
    public String marshall(final Diagram diagram) {
        Definitions definitions = createDefinitions(diagram.getGraph());
        try {
            return mapper.write(definitions);
        } catch (XMLStreamException e) {
            return "";
        }
    }

    Definitions createDefinitions(Graph graph) {
        Iterable<NodeImpl<ViewImpl<BPMNViewDefinition>>> nodes = graph.nodes();
        IdGenerator.reset();
        Definitions definitions = new Definitions();
        BpmnDiagram bpmnDiagram = new BpmnDiagram();
        BpmnPlane plane = new BpmnPlane();
        List<ElementParameters> simulationElements = new ArrayList<>();

        bpmnDiagram.setBpmnPlane(plane);
        definitions.setBpmnDiagram(bpmnDiagram);

        stream(nodes.spliterator(), false)
                .map(node -> node.getContent().getDefinition())
                .filter(node -> node instanceof BpmnContainer)
                .forEach(node -> ((BpmnContainer) node).clear());
        Process process = (Process) stream(nodes.spliterator(), false)
                .map(node -> node.getContent().getDefinition())
                .filter(node -> node instanceof Process)
                .findFirst().orElse(new Process());
        setProcessVariablesToDefinitions(definitions, process.getProperties());
        definitions.setProcess(process);
        plane.setBpmnElement(process.getName());

        List<SequenceFlow> sequenceFlows = new ArrayList<>();
        for (final NodeImpl<ViewImpl<BPMNViewDefinition>> node : nodes) {
            BPMNViewDefinition definition = node.getContent().getDefinition();
            if (definition instanceof Process) {
                Process p = (Process) definition;
                ImportsValue imports = p.getImports().getValue();
                List<WSDLImport> wsdlImports = imports.getWSDLImports();
                for (WSDLImport wsdlImport : wsdlImports) {
                    definitions.getImports().add(new Import(wsdlImport.getLocation(), wsdlImport.getNamespace()));
                }
                continue;
            }

            if (definition instanceof FlowElement) {
                // All except Lanes
                ((FlowElement) definition).setId(IdGenerator.getNextIdFor(definition, node.getUUID()));
            }

            if (definition instanceof StartEvent) {
                StartEvent startEvent = (StartEvent) definition;

                if (startEvent instanceof StartMessageEvent) {
                    StartMessageEvent startMessage = (StartMessageEvent) startEvent;
                    startMessage.setMessageId(IdGenerator.getTypeId(startMessage));
                    GWT.log("Setting Message Id:" + startMessage.getMessageId());

                    definitions.getMessages().add(startMessage.getMessage());
                }

                if (startEvent instanceof StartErrorEvent) {
                    StartErrorEvent startErrorEvent = (StartErrorEvent) startEvent;
                    startErrorEvent.setErrorId(IdGenerator.getTypeId(startErrorEvent));
                    GWT.log("Setting Error Id:" + startErrorEvent.getErrorId());
                    definitions.getErrors().add(startErrorEvent.getError());
                }

                if (startEvent instanceof StartEscalationEvent) {
                    StartEscalationEvent startEscalationEvent = (StartEscalationEvent) startEvent;
                    startEscalationEvent.setEscalationId(IdGenerator.getTypeId(startEscalationEvent));
                    definitions.getEscalations().add(startEscalationEvent.getEscalation());
                }

                if (startEvent instanceof StartSignalEvent) {
                    StartSignalEvent startSignal = (StartSignalEvent) startEvent;
                    startSignal.setSignalId(IdGenerator.getTypeId(startSignal));
                    Signal signal = startSignal.getSignal();
                    if (signal != null) {
                        definitions.getSignals().add(signal);
                    }
                    definitions.getItemDefinitions().addAll(startSignal.getItemDefinition());
                }

                // Adding simulation properties
                simulationElements.add(startEvent.getElementParameters());
            }

            if (definition instanceof EndEvent) {
                EndEvent endEvent = (EndEvent) definition;

                if (endEvent instanceof EndMessageEvent) {
                    EndMessageEvent endMessage = (EndMessageEvent) endEvent;
                    endMessage.setMessageId(IdGenerator.getTypeId(endMessage));
                    definitions.getMessages().add(endMessage.getMessage());
                }

                if (endEvent instanceof EndEscalationEvent) {
                    EndEscalationEvent endEscalationEvent = (EndEscalationEvent) endEvent;
                    endEscalationEvent.setEscalationId(IdGenerator.getTypeId(endEscalationEvent));
                    definitions.getEscalations().add(endEscalationEvent.getEscalation());
                }

                if (endEvent instanceof EndSignalEvent) {
                    EndSignalEvent endSignal = (EndSignalEvent) endEvent;
                    endSignal.setSignalId(IdGenerator.getTypeId(endEvent));
                    Signal signal = endSignal.getSignal();
                    if (signal != null) {
                        definitions.getSignals().add(signal);
                    }
                    definitions.getItemDefinitions().addAll(endSignal.getItemDefinition());
                }

                // Adding simulation properties
                simulationElements.add(endEvent.getElementParameters());
            }

            if (definition instanceof BaseIntermediateEvent) {
                BaseIntermediateEvent intermediateEvent = (BaseIntermediateEvent) definition;

                if (intermediateEvent instanceof IntermediateSignalEventThrowing) {
                    IntermediateSignalEventThrowing throwingSignal = (IntermediateSignalEventThrowing) intermediateEvent;
                    throwingSignal.setSignalId(IdGenerator.getTypeId(intermediateEvent));
                    Signal signal = throwingSignal.getSignal();
                    if (signal != null) {
                        definitions.getSignals().add(signal);
                    }
                    definitions.getItemDefinitions().addAll(throwingSignal.getItemDefinition());
                }

                if (intermediateEvent instanceof IntermediateSignalEventCatching) {
                    IntermediateSignalEventCatching catchingSignal = (IntermediateSignalEventCatching) intermediateEvent;
                    catchingSignal.setSignalId(IdGenerator.getTypeId(intermediateEvent));
                    Signal signal = catchingSignal.getSignal();
                    if (signal != null) {
                        definitions.getSignals().add(signal);
                    }
                    definitions.getItemDefinitions().addAll(catchingSignal.getItemDefinition());
                }

                if (intermediateEvent instanceof IntermediateMessageEventCatching) {
                    IntermediateMessageEventCatching intermediateMessageEventCatching = (IntermediateMessageEventCatching) intermediateEvent;
                    intermediateMessageEventCatching.setMessageId(IdGenerator.getTypeId(intermediateMessageEventCatching));
                    definitions.getMessages().add(intermediateMessageEventCatching.getMessage());
                }

                if (intermediateEvent instanceof IntermediateMessageEventThrowing) {
                    IntermediateMessageEventThrowing intermediateMessageEventThrowing = (IntermediateMessageEventThrowing) intermediateEvent;
                    intermediateMessageEventThrowing.setMessageId(IdGenerator.getTypeId(intermediateMessageEventThrowing));
                    definitions.getMessages().add(intermediateMessageEventThrowing.getMessage());
                }

                if (intermediateEvent instanceof IntermediateErrorEventCatching) {
                    IntermediateErrorEventCatching intermediateErrorEventCatching = (IntermediateErrorEventCatching) intermediateEvent;
                    intermediateErrorEventCatching.setErrorId(IdGenerator.getTypeId(intermediateErrorEventCatching));
                    definitions.getErrors().add(intermediateErrorEventCatching.getError());
                }

                if (intermediateEvent instanceof IntermediateEscalationEvent) {
                    IntermediateEscalationEvent intermediateEscalationEvent = (IntermediateEscalationEvent) intermediateEvent;
                    intermediateEscalationEvent.setEscalationId(IdGenerator.getTypeId(intermediateEscalationEvent));
                    definitions.getEscalations().add(intermediateEscalationEvent.getEscalation());
                }

                if (intermediateEvent instanceof IntermediateEscalationEventThrowing) {
                    IntermediateEscalationEventThrowing intermediateEscalationEventThrowing = (IntermediateEscalationEventThrowing) intermediateEvent;
                    intermediateEscalationEventThrowing.setEscalationId(IdGenerator.getTypeId(intermediateEscalationEventThrowing));
                    definitions.getEscalations().add(intermediateEscalationEventThrowing.getEscalation());
                }

                // Adding simulation properties
                simulationElements.add(intermediateEvent.getElementParameters());
            }

            if (definition instanceof BaseSubprocess) {
                BaseSubprocess subProcess = (BaseSubprocess) definition;
                definitions.getItemDefinitions().addAll(subProcess.getItemDefinitions());
            }

            if (definition instanceof TextAnnotation) {
                node.getInEdges().forEach(edge -> {
                    if (edge.getContent() instanceof ViewConnectorImpl) {
                        ViewConnectorImpl connector = (ViewConnectorImpl) edge.getContent();
                        Association association = (NonDirectionalAssociation) connector.getDefinition();
                        association.setSourceRef(edge.getSourceNode().getUUID());
                        association.setTargetRef(edge.getTargetNode().getUUID());
                        association.setId(IdGenerator.getNextIdFor(association, edge.getUUID()));

                        BpmnEdge edgeNode = new BpmnEdge();
                        edgeNode.setBpmnElement(association.getId());
                        edgeNode.getWaypoint().addAll(createWaypoints(connector));
                        plane.getBpmnEdges().add(edgeNode);

                        // Add association to the same parent as it's TextAnnotation
                        Element parent = GraphUtils.getParent(node);
                        BPMNViewDefinition parentDefinition = ((ViewImpl<BPMNViewDefinition>) parent.getContent()).getDefinition();
                        if (parentDefinition instanceof BpmnContainer) {
                            ((BpmnContainer) parentDefinition).addNode(association);
                        }
                    }
                });
            }

            if (definition instanceof BaseTask) {
                BaseTask task = (BaseTask) definition;
                if (definition instanceof UserTask) {
                    UserTask uTask = (UserTask) task;
                    definitions.getItemDefinitions().addAll(uTask.getItemDefinitions());
                } else if (definition instanceof BusinessRuleTask) {
                    BusinessRuleTask bTask = (BusinessRuleTask) task;
                    definitions.getItemDefinitions().addAll(bTask.getItemDefinitions());
                } else if (definition instanceof CustomTask) {
                    CustomTask cTask = (CustomTask) task;
                    definitions.getItemDefinitions().addAll(cTask.getItemDefinitions());
                } else if (definition instanceof GenericServiceTask) {
                    GenericServiceTask gTask = (GenericServiceTask) task;
                    definitions.getItemDefinitions().addAll(gTask.getItemDefinitions());
                }
            }

            if (definition instanceof Lane) {
                Lane lane = (Lane) definition;
                lane.setId(IdGenerator.getNextIdFor(definition, node.getUUID()));
            }

            if (definition instanceof DataObjectReference) {
                DataObjectReference dataObject = (DataObjectReference) definition;
                definitions.getItemDefinitions().add(dataObject.getItemDefinition());
                process.getDataObjectsReference().add(dataObject);
            }

            Element parent = GraphUtils.getParent(node);
            Object parentDefinition = ((ViewImpl) parent.getContent()).getDefinition();
            parentDefinition = parentDefinition instanceof BpmnContainer ? parentDefinition : process;
            if (parentDefinition instanceof BpmnContainer) {
                List<Edge<? extends ViewConnector<?>, Node>> edges = new ArrayList<>();
                edges.addAll(GraphUtils.getSourceConnections(node));
                edges.addAll(GraphUtils.getTargetConnections(node));
                BpmnContainer finalParentDefinition = (BpmnContainer) parentDefinition;
                finalParentDefinition.addNode(definition);
                edges.forEach(edge -> {
                    ViewConnector connector = edge.getContent();
                    if (connector.getDefinition() instanceof SequenceFlow) {
                        finalParentDefinition.addNode((SequenceFlow) connector.getDefinition());
                    }
                });
            }

            // Adding Shape to Diagram
            BpmnShape bpmnShape = createShapeForBounds(node.getContent().getBounds(), definition.getId());
            if (definition instanceof EmbeddedSubprocess) {
                bpmnShape.setExpanded(true);
            }
            plane.getBpmnShapes().add(bpmnShape);

            if (definition instanceof HasIncoming) {
                HasIncoming hasIncoming = (HasIncoming) definition;
                List<Incoming> incoming = checkIncomingFlows(node.getInEdges(), definition.getId(), sequenceFlows, plane);
                hasIncoming.setIncoming(incoming);
            }

            if (definition instanceof HasOutgoing) {
                HasOutgoing hasOutgoing = (HasOutgoing) definition;
                List<Outgoing> outgoing = checkOutgoingFlows(node.getOutEdges(), definition.getId(), sequenceFlows, plane);
                hasOutgoing.setOutgoing(outgoing);
            }
        }

        // When all IDs are ready
        for (final NodeImpl<ViewImpl<BPMNViewDefinition>> node : nodes) {
            BPMNViewDefinition definition = node.getContent().getDefinition();
            if (definition instanceof Lane) {
                addChildrenToLane(graph, node);
            }
            if (definition instanceof HasActivityRef) {
                HasActivityRef hasActivityRef = (HasActivityRef) definition;
                hasActivityRef.updateActivityRef(IdGenerator.getIdByUuid(hasActivityRef.getActivityRef()));
            }
        }

        // Update associations IDs when all IDs are ready.
        for (Association association : process.getAssociations()) {
            association.setTargetRef(IdGenerator.getIdByUuid(association.getTargetRef()));
            association.setSourceRef(IdGenerator.getIdByUuid(association.getSourceRef()));
            for (BpmnEdge edge : plane.getBpmnEdges()) {
                if (Objects.equals(edge.getBpmnElement(), association.getId())) {
                    edge.setId("edge_shape_" + association.getSourceRef() + "_to_shape_" + association.getTargetRef());
                    break;
                }
            }
        }

        // Set BpmnEdges ids now when all sources and targets are ready
        for (SequenceFlow flow : sequenceFlows) {
            for (BpmnEdge edge : plane.getBpmnEdges()) {
                if (Objects.equals(edge.getBpmnElement(), flow.getId())) {
                    edge.setId("edge_shape_" + flow.getSourceRef() + "_to_shape_" + flow.getTargetRef());
                    break;
                }
            }
        }

        Scenario scenario = new Scenario();
        scenario.setElementParameters(simulationElements);

        BPSimData simData = new BPSimData();
        simData.setScenario(scenario);

        ExtensionElements extensionElements = new ExtensionElements();
        extensionElements.setBpSimData(simData);

        Relationship relationship = new Relationship();
        relationship.setTarget("BPSimData");
        relationship.setExtensionElements(extensionElements);
        relationship.setTarget(definitions.getId());
        relationship.setSource(definitions.getId());
        definitions.setRelationship(relationship);

        return definitions;
    }

    private void addChildrenToLane(Graph graph, NodeImpl<ViewImpl<BPMNViewDefinition>> parent) {
        ChildrenTraverseProcessorImpl processor = new ChildrenTraverseProcessorImpl(new TreeWalkTraverseProcessorImpl());
        processor
                .setRootUUID(parent.getUUID())
                .traverse(graph,
                          new ChildrenTraverseCallback<Node<View, Edge>, Edge<Child, Node>>() {

                              @Override
                              public void startGraphTraversal(Graph<DefinitionSet, Node<View, Edge>> graph) {

                              }

                              @Override
                              public void startEdgeTraversal(Edge<Child, Node> edge) {

                              }

                              @Override
                              public void endEdgeTraversal(Edge<Child, Node> edge) {

                              }

                              @Override
                              public void startNodeTraversal(Node<View, Edge> node) {

                              }

                              @Override
                              public void endNodeTraversal(Node<View, Edge> node) {

                              }

                              @Override
                              public void endGraphTraversal() {

                              }

                              @Override
                              public boolean startNodeTraversal(List<Node<View, Edge>> parents,
                                                                Node<View, Edge> node) {
                                  Lane lane = (Lane) parent.getContent().getDefinition();
                                  if (node.getContent().getDefinition() instanceof FlowElement) {
                                      FlowElement element = (FlowElement) node.getContent().getDefinition();

                                      FlowNodeRef ref = new FlowNodeRef();
                                      ref.setValue(element.getId());
                                      lane.getFlowNodeRefs().add(ref);
                                  }
                                  return false;
                              }
                          });
    }

    private void setProcessVariablesToDefinitions(Definitions definitions, List<Property> processVariables) {
        if (processVariables == null || processVariables.isEmpty()) {
            return;
        }

        processVariables.forEach(processVariable -> {
            ItemDefinition itemDefinition = new ItemDefinition(processVariable.getItemSubjectRef(), processVariable.getVariableType());
            definitions.getItemDefinitions().add(itemDefinition);
        });
    }

    private List<Outgoing> checkOutgoingFlows(List<Edge> edges, String nodeId, List<SequenceFlow> sequenceFlows, BpmnPlane plane) {
        List<Outgoing> outgoing = new ArrayList<>();
        edges.forEach(edge -> {
            if (edge.getContent() instanceof ViewConnectorImpl) {
                ViewConnector connector = (ViewConnector) edge.getContent();
                if (connector.getDefinition() instanceof SequenceFlow) {
                    DiscreteConnection sourceConnection = (DiscreteConnection) connector.getSourceConnection().get();
                    SequenceFlow flow = (SequenceFlow) connector.getDefinition();
                    flow.setAutoConnectionSource(sourceConnection.isAuto());
                    flow.setId(edge.getUUID());
                    updateOutgoingFlow(flow, nodeId, sequenceFlows, createWaypoints(connector), plane);

                    outgoing.add(new Outgoing(flow.getId()));
                }
            }
        });
        return outgoing;
    }

    private List<Incoming> checkIncomingFlows(List<Edge> edges, String nodeId, List<SequenceFlow> sequenceFlows, BpmnPlane plane) {
        List<Incoming> incoming = new ArrayList<>();
        edges.forEach(edge -> {
            if (edge.getContent() instanceof ViewConnectorImpl) {
                ViewConnector connector = (ViewConnector) edge.getContent();
                if (connector.getDefinition() instanceof SequenceFlow) {
                    DiscreteConnection targetConnection = (DiscreteConnection) connector.getTargetConnection().get();
                    SequenceFlow flow = (SequenceFlow) connector.getDefinition();
                    flow.setAutoConnectionTarget(targetConnection.isAuto());
                    flow.setId(edge.getUUID());

                    addIncomingFlow(flow, nodeId, sequenceFlows, createWaypoints(connector), plane);

                    incoming.add(new Incoming(flow.getId()));
                }
            }
        });
        return incoming;
    }

    private void updateOutgoingFlow(SequenceFlow flow, String id, List<SequenceFlow> sequenceFlows, List<Waypoint> waypoints, BpmnPlane plane) {
        for (SequenceFlow f : sequenceFlows) {
            if (Objects.equals(flow.getId(), f.getId())) {
                f.setSourceRef(id);
                return;
            }
        }

        flow.setSourceRef(id);
        sequenceFlows.add(flow);
        BpmnEdge edge = createBpmnEdge(flow, waypoints);
        plane.getBpmnEdges().add(edge);
    }

    private void addIncomingFlow(SequenceFlow flow, String id, List<SequenceFlow> sequenceFlows, List<Waypoint> waypoints, BpmnPlane plane) {
        for (SequenceFlow f : sequenceFlows) {
            if (Objects.equals(flow.getId(), f.getId())) {
                f.setTargetRef(id);
                return;
            }
        }

        flow.setTargetRef(id);
        sequenceFlows.add(flow);
        BpmnEdge edge = createBpmnEdge(flow, waypoints);
        plane.getBpmnEdges().add(edge);
    }

    private List<Waypoint> createWaypoints(ViewConnector connector) {
        List<Waypoint> waypoints = new ArrayList<>();

        // Source point
        Point2D sourcePoint = ((Connection) connector.getSourceConnection().get()).getLocation();
        Waypoint source = new Waypoint(sourcePoint.getX(), sourcePoint.getY());
        waypoints.add(source);

        // Waypoints
        for (ControlPoint point : connector.getControlPoints()) {
            waypoints.add(new Waypoint(
                                  point.getLocation().getX(),
                                  point.getLocation().getY()
                          )
            );
        }

        // Target point
        Point2D targetPoint = ((Connection) connector.getTargetConnection().get()).getLocation();
        Waypoint target = new Waypoint(targetPoint.getX(), targetPoint.getY());
        waypoints.add(target);

        return waypoints;
    }

    private BpmnEdge createBpmnEdge(SequenceFlow flow, List<Waypoint> waypoints) {
        BpmnEdge edge = new BpmnEdge();
        // ID can't be set yet, since target is not set yet.
        edge.setBpmnElement(flow.getId());

        edge.setWaypoint(waypoints);
        return edge;
    }

    private BpmnShape createShapeForBounds(final org.kie.workbench.common.stunner.core.graph.content.Bounds bounds, final String id) {
        BpmnShape shape = new BpmnShape("shape_" + id, id);
        Bounds b = new Bounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        shape.setBounds(b);

        return shape;
    }

    public Definitions unmarshall(final String raw) {
        try {
            return mapper.read(raw);
        } catch (XMLStreamException e) {
            return new org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Definitions();
        }
    }

    public static String getDefinitionSetId() {
        return BindableAdapterUtils.getDefinitionSetId(BPMNDefinitionSet.class);
    }
}
