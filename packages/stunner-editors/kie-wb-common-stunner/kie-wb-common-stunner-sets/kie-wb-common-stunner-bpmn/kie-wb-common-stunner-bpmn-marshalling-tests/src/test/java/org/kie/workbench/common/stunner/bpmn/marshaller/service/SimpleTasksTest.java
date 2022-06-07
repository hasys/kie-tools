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
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.CustomTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.NoneTask;
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
public class SimpleTasksTest {

    private final Marshaller marshaller = new Marshaller();

    private Process processOld;

    @Before
    public void init() throws Exception {
        String PATH = "org/kie/workbench/common/stunner/bpmn/marshaller/model/";
        String FILE_NAME_OLD = PATH + "Tasks (OLD).bpmn";
        Diagram<Graph, Metadata> diagramOld = Unmarshalling.unmarshall(marshaller, FILE_NAME_OLD);
        processOld = getProcess(diagramOld, "process");
    }

    @Test
    public void testOldIntermediateThrowEvents() {
        List<BaseTask> tasks = processOld.getTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.get(0) instanceof CustomTask);
        CustomTask customTask = (CustomTask) tasks.get(0);
        assertEquals("_72F39BB7-0FE5-482D-8E2B-F3A23592E798", customTask.getId());
        assertEquals("Milestone Name", customTask.getName());
        assertEquals("Milestone Documentation", customTask.getDocumentation());
        assertEquals("metaNameßmetaValue", customTask.getAdvancedData().getMetaDataAttributes());

        assertTrue(tasks.get(1) instanceof NoneTask);
        NoneTask noneTask = (NoneTask) tasks.get(1);
        assertEquals("_A019DC23-46DC-4B63-9383-EF10BCCB4DED", noneTask.getId());
        assertEquals("Task Name", noneTask.getName());
        assertEquals("Task Documentation", noneTask.getDocumentation());
        assertEquals("metaNameßmetaValue", noneTask.getAdvancedData().getMetaDataAttributes());
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