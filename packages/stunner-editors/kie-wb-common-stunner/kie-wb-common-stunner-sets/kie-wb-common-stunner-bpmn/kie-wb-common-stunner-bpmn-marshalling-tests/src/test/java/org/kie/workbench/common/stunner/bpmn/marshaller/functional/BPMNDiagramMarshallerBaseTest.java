/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.marshaller.functional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kie.workbench.common.stunner.bpmn.definition.BaseElement;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Definitions;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Process;
import org.kie.workbench.common.stunner.bpmn.marshaller.Marshaller;
import org.kie.workbench.common.stunner.bpmn.marshaller.Unmarshalling;
import org.kie.workbench.common.stunner.core.definition.service.DiagramMarshaller;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.kie.workbench.common.stunner.core.graph.Graph;
import org.kie.workbench.common.stunner.core.graph.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public abstract class BPMNDiagramMarshallerBaseTest {

    protected Marshaller marshaller = new Marshaller();

    protected void assertDiagram(Diagram<Graph, Metadata> diagram, int nodesSize) {
        assertEquals(nodesSize, getNodes(diagram).size());
    }

    @SuppressWarnings("unchecked")
    protected List<Node> getNodes(Diagram<Graph, Metadata> diagram) {
        Graph graph = diagram.getGraph();
        assertNotNull(graph);
        Iterator<Node> nodesIterable = graph.nodes().iterator();
        List<Node> nodes = new ArrayList<>();
        nodesIterable.forEachRemaining(nodes::add);

        Process process = (Process) nodes.get(0); // Process

        return nodes;
    }

    protected List<BaseElement> getNodesAsBaseElements(Process process) {
        List<BaseElement> result = new ArrayList<>();
        result.add(process);
        result.addAll(process.getNodesAsBaseElements());
        return result;
    }

    protected Diagram<Graph, Metadata> unmarshall(DiagramMarshaller<Graph, Metadata, Diagram<Graph, Metadata>> marshaller, String fileName) throws Exception {
        return Unmarshalling.unmarshall(marshaller, fileName);
    }

    protected Diagram<Graph, Metadata> unmarshall(DiagramMarshaller<Graph, Metadata, Diagram<Graph, Metadata>> marshaller, InputStream is) throws Exception {
        return Unmarshalling.unmarshall(marshaller, is);
    }

    protected InputStream getStream(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }

    public static InputStream loadStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}
