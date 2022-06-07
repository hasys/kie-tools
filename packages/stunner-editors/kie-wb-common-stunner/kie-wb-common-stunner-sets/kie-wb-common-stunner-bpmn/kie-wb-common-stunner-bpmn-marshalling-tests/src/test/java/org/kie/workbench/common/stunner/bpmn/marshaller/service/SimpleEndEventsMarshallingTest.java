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
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndCompensationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndErrorEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndMessageEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndNoneEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndSignalEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndTerminateEvent;
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
public class SimpleEndEventsMarshallingTest {

    private final Marshaller marshaller = new Marshaller();

    private Process processOld;

    @Before
    public void init() throws Exception {
        String PATH = "org/kie/workbench/common/stunner/bpmn/marshaller/model/";
        String FILE_NAME_OLD = PATH + "End Events (OLD).bpmn";
        Diagram<Graph, Metadata> diagramOld = Unmarshalling.unmarshall(marshaller, FILE_NAME_OLD);
        processOld = getProcess(diagramOld, "process");
    }

    @Test
    public void testOldEndEvents() {
        List<EndEvent> endEvents = processOld.getEndEvents();
        assertEquals(7, endEvents.size());
        assertTrue(endEvents.get(0) instanceof EndCompensationEvent);
        EndCompensationEvent compensationEvent = (EndCompensationEvent) endEvents.get(0);
        assertEquals("_0B5B856D-FA72-4CC8-B7E4-509FDB9AEE45", compensationEvent.getId());
        assertEquals("End Compensation Name", compensationEvent.getName());
        assertEquals("End Compensation Documentation", compensationEvent.getDocumentation());
        assertEquals("MetaName_OneßMetaValue_OneØMetaName_TwoßMetaValue_Two", compensationEvent.getAdvancedData().getMetaDataAttributes());
        assertTrue(endEvents.get(1) instanceof EndEscalationEvent);
        EndEscalationEvent escalationEvent = (EndEscalationEvent) endEvents.get(1);
        assertEquals("_803009139", escalationEvent.getExecutionSet().getEscalationRef().getValue());
        assertEquals("EscalationRefValue", escalationEvent.getEscalationEventDefinition().getEsccode());
        assertTrue(endEvents.get(2) instanceof EndErrorEvent);
        assertTrue(endEvents.get(3) instanceof EndTerminateEvent);
        assertTrue(endEvents.get(4) instanceof EndMessageEvent);
        assertTrue(endEvents.get(5) instanceof EndSignalEvent);
        assertTrue(endEvents.get(6) instanceof EndNoneEvent);
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