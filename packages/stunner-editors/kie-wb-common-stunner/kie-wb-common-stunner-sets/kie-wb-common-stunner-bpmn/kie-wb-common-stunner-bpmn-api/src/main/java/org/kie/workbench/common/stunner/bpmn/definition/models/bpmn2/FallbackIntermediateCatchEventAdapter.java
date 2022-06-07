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

import org.kie.workbench.common.stunner.bpmn.definition.hasCustomSLADueDate;

public class FallbackIntermediateCatchEventAdapter extends XmlAdapter<FallbackIntermediateCatchEvent, BaseCatchingIntermediateEvent> {

    @Override
    public BaseCatchingIntermediateEvent unmarshal(FallbackIntermediateCatchEvent v) {
        BaseCatchingIntermediateEvent result = null;

        if (v.getEventDefinition() instanceof CompensateEventDefinition) {
            result = new IntermediateCompensationEvent();
            IntermediateCompensationEvent intermediateCatchCompensationEvent = (IntermediateCompensationEvent) result;
            intermediateCatchCompensationEvent.setCompensateEventDefinition((CompensateEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof MessageEventDefinition) {
            result = new IntermediateMessageEventCatching();
            IntermediateMessageEventCatching intermediateCatchMessageEvent = (IntermediateMessageEventCatching) result;
            intermediateCatchMessageEvent.setMessageEventDefinition((MessageEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof SignalEventDefinition) {
            result = new IntermediateSignalEventCatching();
            IntermediateSignalEventCatching intermediateCatchSignalEvent = (IntermediateSignalEventCatching) result;
            intermediateCatchSignalEvent.setSignalEventDefinition((SignalEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof ErrorEventDefinition) {
            result = new IntermediateErrorEventCatching();
            IntermediateErrorEventCatching intermediateCatchErrorEvent = (IntermediateErrorEventCatching) result;
            intermediateCatchErrorEvent.setErrorEventDefinition((ErrorEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof EscalationEventDefinition) {
            result = new IntermediateEscalationEvent();
            IntermediateEscalationEvent intermediateCatchEscalationEvent = (IntermediateEscalationEvent) result;
            intermediateCatchEscalationEvent.setEscalationEventDefinition((EscalationEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof ConditionalEventDefinition) {
            result = new IntermediateConditionalEvent();
            IntermediateConditionalEvent intermediateCatchConditionalEvent = (IntermediateConditionalEvent) result;
            intermediateCatchConditionalEvent.setConditionalEventDefinition((ConditionalEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof TimerEventDefinition) {
            result = new IntermediateTimerEvent();
            IntermediateTimerEvent intermediateCatchTimerEvent = (IntermediateTimerEvent) result;
            intermediateCatchTimerEvent.setTimerEventDefinition((TimerEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof LinkEventDefinition) {
            IntermediateLinkEventCatching catching = new IntermediateLinkEventCatching();
            catching.setLinkEventDefinition((LinkEventDefinition) v.getEventDefinition());
            result = catching;
        }

        if (result instanceof hasCustomSLADueDate) {
            ((hasCustomSLADueDate) result).setSlaDueDate(v.getExtensionElements());
        }

        FallbackFlowNodeAdapter.unmarshal(result, v);

        return result;
    }

    @Override
    public FallbackIntermediateCatchEvent marshal(BaseCatchingIntermediateEvent v) {
        FallbackIntermediateCatchEvent fallbackIntermediateCatchEvent = new FallbackIntermediateCatchEvent();
        fallbackIntermediateCatchEvent.setId(v.getId());
        fallbackIntermediateCatchEvent.setName(v.getName());
        fallbackIntermediateCatchEvent.setDocumentation(v.getDocumentation());
        fallbackIntermediateCatchEvent.setExtensionElements(v.getExtensionElements());

        if (v instanceof IntermediateCompensationEvent) {
            fallbackIntermediateCatchEvent.setEventDefinition(new CompensateEventDefinition());
        }
        if (v instanceof IntermediateMessageEventCatching) {
            fallbackIntermediateCatchEvent.setEventDefinition(new MessageEventDefinition());
        }
        if (v instanceof IntermediateSignalEventCatching) {
            fallbackIntermediateCatchEvent.setEventDefinition(new SignalEventDefinition());
        }
        if (v instanceof IntermediateErrorEventCatching) {
            fallbackIntermediateCatchEvent.setEventDefinition(new ErrorEventDefinition());
        }
        if (v instanceof IntermediateEscalationEvent) {
            fallbackIntermediateCatchEvent.setEventDefinition(new EscalationEventDefinition());
        }
        if (v instanceof IntermediateConditionalEvent) {
            fallbackIntermediateCatchEvent.setEventDefinition(new ConditionalEventDefinition());
        }
        if (v instanceof IntermediateTimerEvent) {
            fallbackIntermediateCatchEvent.setEventDefinition(new TimerEventDefinition());
        }
        if (v instanceof IntermediateLinkEventCatching) {
            fallbackIntermediateCatchEvent.setEventDefinition(new LinkEventDefinition());
        }

        if (v instanceof hasCustomSLADueDate) {
            ((hasCustomSLADueDate) v).setSlaDueDate(v.getExtensionElements());
        }
        return fallbackIntermediateCatchEvent;
    }
}