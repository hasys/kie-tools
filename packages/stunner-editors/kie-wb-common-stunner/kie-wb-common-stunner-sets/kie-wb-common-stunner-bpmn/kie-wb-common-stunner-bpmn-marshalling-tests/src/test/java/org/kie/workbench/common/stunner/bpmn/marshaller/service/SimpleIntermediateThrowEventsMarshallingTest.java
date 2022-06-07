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
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseThrowingIntermediateEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateCompensationEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateEscalationEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateLinkEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateMessageEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateSignalEventThrowing;
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
public class SimpleIntermediateThrowEventsMarshallingTest {

    private final Marshaller marshaller = new Marshaller();

    private Process processOld;

    @Before
    public void init() throws Exception {
        String PATH = "org/kie/workbench/common/stunner/bpmn/marshaller/model/";
        String FILE_NAME_OLD = PATH + "Throw Intermediate Events (OLD).bpmn";
        Diagram<Graph, Metadata> diagramOld = Unmarshalling.unmarshall(marshaller, FILE_NAME_OLD);
        processOld = getProcess(diagramOld, "process");
    }

    @Test
    public void testOldIntermediateThrowEvents() {
        List<BaseThrowingIntermediateEvent> intermediateThrowEvents = processOld.getIntermediateThrowEvent();
        assertEquals(5, intermediateThrowEvents.size());
        assertTrue(intermediateThrowEvents.get(0) instanceof IntermediateCompensationEventThrowing);
        IntermediateCompensationEventThrowing compensationEvent = (IntermediateCompensationEventThrowing) intermediateThrowEvents.get(0);
        assertEquals("_ABF52261-B9D7-4968-99B3-AFD192EAEB23", compensationEvent.getId());
        assertEquals("Intermediate Throw Compensation Name", compensationEvent.getName());
        assertEquals("Intermediate Throw Compensation Documentation", compensationEvent.getDocumentation());
        assertEquals("metaNameßmetaValue", compensationEvent.getAdvancedData().getMetaDataAttributes());
        assertTrue(intermediateThrowEvents.get(1) instanceof IntermediateEscalationEventThrowing);
        IntermediateEscalationEventThrowing escalationEvent = (IntermediateEscalationEventThrowing) intermediateThrowEvents.get(1);
        assertEquals("_1538476237", escalationEvent.getExecutionSet().getEscalationRef().getValue());
        assertEquals("escalationNameCode", escalationEvent.getEscalationEventDefinition().getEsccode());
        assertTrue(intermediateThrowEvents.get(2) instanceof IntermediateMessageEventThrowing);
        assertTrue(intermediateThrowEvents.get(3) instanceof IntermediateLinkEventThrowing);
        assertTrue(intermediateThrowEvents.get(4) instanceof IntermediateSignalEventThrowing);
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