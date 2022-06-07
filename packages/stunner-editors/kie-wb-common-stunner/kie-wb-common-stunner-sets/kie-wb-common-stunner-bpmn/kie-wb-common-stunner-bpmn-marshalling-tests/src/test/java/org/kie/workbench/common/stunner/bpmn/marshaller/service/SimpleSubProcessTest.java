/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.stunner.bpmn.marshaller.service;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EmbeddedSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EventSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.MultipleInstanceSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Process;
import org.kie.workbench.common.stunner.bpmn.marshaller.Marshaller;
import org.kie.workbench.common.stunner.bpmn.marshaller.Unmarshalling;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.kie.workbench.common.stunner.core.graph.Graph;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SimpleSubProcessTest {

    private final Marshaller marshaller = new Marshaller();

    private Process processOld;

    @Before
    public void init() throws Exception {
        String PATH = "org/kie/workbench/common/stunner/bpmn/marshaller/model/";
        String FILE_NAME_OLD = PATH + "SubProcess (Old).bpmn";
        Diagram<Graph, Metadata> diagramOld = Unmarshalling.unmarshall(marshaller, FILE_NAME_OLD);
        processOld = getProcess(diagramOld, "process");
    }

    @Test
    public void testOldIntermediateThrowEvents() {
        List<BaseSubprocess> subProcesses = processOld.getSubProcesses();
        assertEquals(3, subProcesses.size());
        assertTrue(subProcesses.get(0) instanceof EmbeddedSubprocess);
        EmbeddedSubprocess embeddedSubprocess = (EmbeddedSubprocess) subProcesses.get(0);
        assertEquals("_EBADC595-38CA-40E8-A63F-74CA1905AC17", embeddedSubprocess.getId());
        assertEquals("Embedded Sub-process Name", embeddedSubprocess.getName());
        assertEquals("Embedded Sub-process Documentation", embeddedSubprocess.getDocumentation());
        assertEquals("metaNameßmetaValue", embeddedSubprocess.getAdvancedData().getMetaDataAttributes());

        assertTrue(subProcesses.get(1) instanceof MultipleInstanceSubprocess);
        MultipleInstanceSubprocess multipleInstanceSubprocess = (MultipleInstanceSubprocess) subProcesses.get(1);
        assertEquals("_6490C3E5-858A-4647-8246-88ADE2C8AD0E", multipleInstanceSubprocess.getId());
        assertEquals("Multiple Instance Sub-process Name", multipleInstanceSubprocess.getName());
        assertEquals("Multiple Instance Sub-process Documentation", multipleInstanceSubprocess.getDocumentation());
        assertEquals("metaNameßmetaValue", multipleInstanceSubprocess.getAdvancedData().getMetaDataAttributes());

        assertTrue(subProcesses.get(2) instanceof EventSubprocess);
        EventSubprocess eventSubprocess = (EventSubprocess) subProcesses.get(2);
        assertEquals("_EB580A99-14B0-44BB-8E3B-90137E71ED49", eventSubprocess.getId());
        assertEquals("Event Sub-process Name", eventSubprocess.getName());
        assertEquals("Event Sub-process Documentation", eventSubprocess.getDocumentation());
        assertEquals("metaNameßmetaValue", eventSubprocess.getAdvancedData().getMetaDataAttributes());
    }

    @SuppressWarnings("unchecked")
    private Process getProcess(Diagram<Graph, Metadata> diagram, String id) {
        Node<? extends Process, ?> node = getNodeById(diagram, id);
        return node.getContent();
    }

    @SuppressWarnings("unchecked")
    private Node<? extends Process, ?> getNodeById(Diagram<Graph, Metadata> diagram, String id) {
        Node<? extends Process, ?> node = diagram.getGraph().getNode(id);
        assertThat(node).isNotNull();
        return node;
    }
}