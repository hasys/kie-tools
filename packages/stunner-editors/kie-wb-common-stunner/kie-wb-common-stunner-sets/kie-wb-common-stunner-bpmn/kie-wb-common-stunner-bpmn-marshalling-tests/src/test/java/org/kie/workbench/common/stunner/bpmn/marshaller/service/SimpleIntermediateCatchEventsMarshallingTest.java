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
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseCatchingIntermediateEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateCompensationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateConditionalEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateErrorEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateLinkEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateMessageEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateSignalEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateTimerEvent;
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
public class SimpleIntermediateCatchEventsMarshallingTest {

    private final Marshaller marshaller = new Marshaller();

    private final String PATH = "org/kie/workbench/common/stunner/bpmn/marshaller/model/";

    private final String FILE_NAME_OLD = PATH + "Catch Intermediate Events (OLD).bpmn";

    private Diagram<Graph, Metadata> diagramOld;

    private Process processOld;

    @Before
    public void init() throws Exception {
        diagramOld = Unmarshalling.unmarshall(marshaller, FILE_NAME_OLD);
        processOld = getProcess(diagramOld, "process");
    }

    @Test
    public void testOldIntermediateCatchEvents() {
        List<BaseCatchingIntermediateEvent> intermediateCatchEvents = processOld.getIntermediateCatchEvent();
        assertEquals(8, intermediateCatchEvents.size());
        assertTrue(intermediateCatchEvents.get(0) instanceof IntermediateConditionalEvent);
        assertTrue(intermediateCatchEvents.get(1) instanceof IntermediateCompensationEvent);
        IntermediateCompensationEvent compensationEvent = (IntermediateCompensationEvent) intermediateCatchEvents.get(1);
        assertEquals("_669DEDF8-55E0-4E53-8681-2C7E82AAB122", compensationEvent.getId());
        assertEquals("Intermediate Catch Compensation Name", compensationEvent.getName());
        assertEquals("Intermediate Catch Compensation Documentation", compensationEvent.getDocumentation());
        assertEquals("MetaNameßMetaValue", compensationEvent.getAdvancedData().getMetaDataAttributes());
        assertTrue(intermediateCatchEvents.get(2) instanceof IntermediateEscalationEvent);
        IntermediateEscalationEvent escalationEvent = (IntermediateEscalationEvent) intermediateCatchEvents.get(2);
        assertEquals("_-1807190538", escalationEvent.getExecutionSet().getEscalationRef().getValue());
        assertEquals("escalationRefCodeName", escalationEvent.getEscalationEventDefinition().getEsccode());
        assertTrue(intermediateCatchEvents.get(3) instanceof IntermediateErrorEventCatching);
        assertTrue(intermediateCatchEvents.get(4) instanceof IntermediateLinkEventCatching);
        assertTrue(intermediateCatchEvents.get(5) instanceof IntermediateSignalEventCatching);
        assertTrue(intermediateCatchEvents.get(6) instanceof IntermediateMessageEventCatching);
        assertTrue(intermediateCatchEvents.get(7) instanceof IntermediateTimerEvent);
        IntermediateTimerEvent timerEvent = (IntermediateTimerEvent) intermediateCatchEvents.get(7);
        assertEquals("PT1H", timerEvent.getTimerEventDefinition().getTimeDuration().getValue());
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