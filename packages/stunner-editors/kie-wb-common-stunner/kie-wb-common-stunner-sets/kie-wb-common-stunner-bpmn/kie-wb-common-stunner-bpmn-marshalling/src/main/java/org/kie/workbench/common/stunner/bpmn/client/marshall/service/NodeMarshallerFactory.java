package org.kie.workbench.common.stunner.bpmn.client.marshall.service;

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.FlowNode;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmndi.BpmnShape;
import org.kie.workbench.common.stunner.core.factory.impl.NodeFactoryImpl;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.Bounds;
import org.kie.workbench.common.stunner.core.graph.content.definition.Definition;
import org.kie.workbench.common.stunner.core.graph.content.relationship.Child;
import org.kie.workbench.common.stunner.core.graph.content.view.View;
import org.kie.workbench.common.stunner.core.graph.impl.EdgeImpl;
import org.kie.workbench.common.stunner.core.util.DefinitionUtils;
import org.kie.workbench.common.stunner.core.util.UUID;

public class NodeMarshallerFactory {

    private final NodeFactoryImpl nodeFactory;
    private final DefinitionUtils definitionUtils;
    private final Definitions definitions;

    public NodeMarshallerFactory(NodeFactoryImpl nodeFactory, DefinitionUtils definitionUtils, Definitions definitions) {
        this.nodeFactory = nodeFactory;
        this.definitionUtils = definitionUtils;
        this.definitions = definitions;
    }

    @SuppressWarnings({"rawtypes"})
    public Node<Definition<Object>, Edge> buildNode(FlowNode node, Node<Definition<Object>, ?> parent) {
        return buildNode(node.getId(), node, parent);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Node<Definition<Object>, Edge> buildNode(String id, Object definition, Node<Definition<Object>, ?> parent) {
        for (BpmnShape shape : definitions.getBpmnDiagram().getBpmnPlane().getBpmnShapes()) {
            if (shape.getBpmnElement().equals(id)) {
                final Node<Definition<Object>, Edge> build = nodeFactory.build(id, definition);
                View<Object> content = (View) build.getContent();
                Bounds bounds = definitionUtils.buildBounds(definition, shape.getBounds().getX(), shape.getBounds().getY());
                bounds.getLowerRight().setX(shape.getBounds().getX() + shape.getBounds().getWidth());
                bounds.getLowerRight().setY(shape.getBounds().getY() + shape.getBounds().getHeight());
                content.setBounds(bounds);
                connectRootWithChild(parent, build);
                return build;
            }
        }

        return null;
    }

    @SuppressWarnings({"rawtypes"})
    private void connectRootWithChild(final Node parent,
                                      final Node child) {
        final String uuid = UUID.uuid();
        final Edge<Child, Node> edge = new EdgeImpl<>(uuid);
        edge.setContent(new Child());
        connectEdge(edge, parent, child);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void connectEdge(final Edge edge,
                             final Node source,
                             final Node target) {
        edge.setSourceNode(source);
        edge.setTargetNode(target);
        source.getOutEdges().add(edge);
        target.getInEdges().add(edge);
    }
}
