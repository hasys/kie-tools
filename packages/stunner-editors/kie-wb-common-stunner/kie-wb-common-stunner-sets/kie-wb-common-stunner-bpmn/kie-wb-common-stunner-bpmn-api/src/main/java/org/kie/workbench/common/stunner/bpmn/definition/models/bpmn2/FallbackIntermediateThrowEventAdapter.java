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

public class FallbackIntermediateThrowEventAdapter extends XmlAdapter<FallbackIntermediateThrowEvent, BaseIntermediateEvent> {

    @Override
    public BaseIntermediateEvent unmarshal(FallbackIntermediateThrowEvent v) {
        BaseIntermediateEvent result = null;

        if (v.getEventDefinition() instanceof CompensateEventDefinition) {
            result = new IntermediateCompensationEventThrowing();
            IntermediateCompensationEventThrowing startCompensationEvent = (IntermediateCompensationEventThrowing) result;
            startCompensationEvent.setCompensateEventDefinition((CompensateEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof MessageEventDefinition) {
            result = new IntermediateMessageEventThrowing();
            IntermediateMessageEventThrowing startMessageEvent = (IntermediateMessageEventThrowing) result;
            startMessageEvent.setMessageEventDefinition((MessageEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof SignalEventDefinition) {
            result = new IntermediateSignalEventThrowing();
            IntermediateSignalEventThrowing startSignalEvent = (IntermediateSignalEventThrowing) result;
            startSignalEvent.setSignalEventDefinition((SignalEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof LinkEventDefinition) {
            result = new IntermediateLinkEventThrowing();
            IntermediateLinkEventThrowing endErrorEvent = (IntermediateLinkEventThrowing) result;
            endErrorEvent.setLinkEventDefinition((LinkEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof EscalationEventDefinition) {
            result = new IntermediateEscalationEventThrowing();
            IntermediateEscalationEventThrowing endEscalationEvent = (IntermediateEscalationEventThrowing) result;
            endEscalationEvent.setEscalationEventDefinition((EscalationEventDefinition) v.getEventDefinition());
        }

        FallbackFlowNodeAdapter.unmarshal(result, v);

        return result;
    }

    @Override
    public FallbackIntermediateThrowEvent marshal(BaseIntermediateEvent v) {
        FallbackIntermediateThrowEvent fallbackIntermediateThrowEvent = new FallbackIntermediateThrowEvent();
        fallbackIntermediateThrowEvent.setId(v.getId());
        fallbackIntermediateThrowEvent.setName(v.getName());
        fallbackIntermediateThrowEvent.setDocumentation(v.getDocumentation());
        fallbackIntermediateThrowEvent.setExtensionElements(v.getExtensionElements());

        if (v instanceof IntermediateCompensationEventThrowing) {
            fallbackIntermediateThrowEvent.setEventDefinition(new CompensateEventDefinition());
        }
        if (v instanceof IntermediateMessageEventThrowing) {
            fallbackIntermediateThrowEvent.setEventDefinition(new MessageEventDefinition());
        }
        if (v instanceof IntermediateSignalEventThrowing) {
            fallbackIntermediateThrowEvent.setEventDefinition(new SignalEventDefinition());
        }
        if (v instanceof IntermediateLinkEventThrowing) {
            fallbackIntermediateThrowEvent.setEventDefinition(new LinkEventDefinition());
        }
        if (v instanceof IntermediateEscalationEventThrowing) {
            fallbackIntermediateThrowEvent.setEventDefinition(new EscalationEventDefinition());
        }

        return fallbackIntermediateThrowEvent;
    }
}