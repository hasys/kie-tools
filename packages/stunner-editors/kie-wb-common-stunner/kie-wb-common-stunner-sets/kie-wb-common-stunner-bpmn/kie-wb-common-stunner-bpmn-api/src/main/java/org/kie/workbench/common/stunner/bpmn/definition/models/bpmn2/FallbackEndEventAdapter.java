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
package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FallbackEndEventAdapter extends XmlAdapter<FallbackEndEvent, EndEvent> {

    @Override
    public EndEvent unmarshal(FallbackEndEvent v) {
        EndEvent result;

        if (v.getEventDefinition() instanceof CompensateEventDefinition) {
            result = new EndCompensationEvent();
            EndCompensationEvent startCompensationEvent = (EndCompensationEvent) result;
            startCompensationEvent.setCompensateEventDefinition((CompensateEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof MessageEventDefinition) {
            result = new EndMessageEvent();
            EndMessageEvent startMessageEvent = (EndMessageEvent) result;
            startMessageEvent.setMessageEventDefinition((MessageEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof SignalEventDefinition) {
            result = new EndSignalEvent();
            EndSignalEvent startSignalEvent = (EndSignalEvent) result;
            startSignalEvent.setSignalEventDefinition((SignalEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof ErrorEventDefinition) {
            result = new EndErrorEvent();
            EndErrorEvent endErrorEvent = (EndErrorEvent) result;
            endErrorEvent.setErrorEventDefinition((ErrorEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof EscalationEventDefinition) {
            result = new EndEscalationEvent();
            EndEscalationEvent endEscalationEvent = (EndEscalationEvent) result;
            endEscalationEvent.setEscalationEventDefinition((EscalationEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof TerminateEventDefinition) {
            result = new EndTerminateEvent();
        } else {
            result = new EndNoneEvent();
        }

        FallbackFlowNodeAdapter.unmarshal(result, v);

        return result;
    }

    @Override
    public FallbackEndEvent marshal(EndEvent v) {
        FallbackEndEvent fallbackEndEvent = new FallbackEndEvent();
        fallbackEndEvent.setId(v.getId());
        fallbackEndEvent.setName(v.getName());
        fallbackEndEvent.setDocumentation(v.getDocumentation());
        fallbackEndEvent.setExtensionElements(v.getExtensionElements());

        if (v instanceof EndCompensationEvent) {
            fallbackEndEvent.setEventDefinition(new CompensateEventDefinition());
        }
        if (v instanceof EndMessageEvent) {
            fallbackEndEvent.setEventDefinition(new MessageEventDefinition());
        }
        if (v instanceof EndSignalEvent) {
            fallbackEndEvent.setEventDefinition(new SignalEventDefinition());
        }
        if (v instanceof EndErrorEvent) {
            fallbackEndEvent.setEventDefinition(new ErrorEventDefinition());
        }
        if (v instanceof EndEscalationEvent) {
            fallbackEndEvent.setEventDefinition(new EscalationEventDefinition());
        }

        return fallbackEndEvent;
    }
}