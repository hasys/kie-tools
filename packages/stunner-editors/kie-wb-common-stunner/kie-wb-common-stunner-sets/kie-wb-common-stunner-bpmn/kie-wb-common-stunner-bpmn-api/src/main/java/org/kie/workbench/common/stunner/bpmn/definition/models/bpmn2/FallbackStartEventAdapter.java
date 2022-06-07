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
import org.kie.workbench.common.stunner.bpmn.definition.property.event.BaseStartEventExecutionSet;

public class FallbackStartEventAdapter extends XmlAdapter<FallbackStartEvent, StartEvent> {

    @Override
    public StartEvent unmarshal(FallbackStartEvent v) {
        StartEvent result;

        if (v.getEventDefinition() instanceof CompensateEventDefinition) {
            result = new StartCompensationEvent();
            StartCompensationEvent startCompensationEvent = (StartCompensationEvent) result;
            startCompensationEvent.setCompensateEventDefinition((CompensateEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof MessageEventDefinition) {
            result = new StartMessageEvent();
            StartMessageEvent startMessageEvent = (StartMessageEvent) result;
            startMessageEvent.setMessageEventDefinition((MessageEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof SignalEventDefinition) {
            result = new StartSignalEvent();
            StartSignalEvent startSignalEvent = (StartSignalEvent) result;
            startSignalEvent.setSignalEventDefinition((SignalEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof ErrorEventDefinition) {
            result = new StartErrorEvent();
            StartErrorEvent startErrorEvent = (StartErrorEvent) result;
            startErrorEvent.setErrorEventDefinition((ErrorEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof EscalationEventDefinition) {
            result = new StartEscalationEvent();
            StartEscalationEvent startEscalationEvent = (StartEscalationEvent) result;
            startEscalationEvent.setEscalationEventDefinition((EscalationEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof ConditionalEventDefinition) {
            result = new StartConditionalEvent();
            StartConditionalEvent startConditionalEvent = (StartConditionalEvent) result;
            startConditionalEvent.setConditionalEventDefinition((ConditionalEventDefinition) v.getEventDefinition());
        } else if (v.getEventDefinition() instanceof TimerEventDefinition) {
            result = new StartTimerEvent();
            StartTimerEvent startTimerEvent = (StartTimerEvent) result;
            startTimerEvent.setTimerEventDefinition((TimerEventDefinition) v.getEventDefinition());
        } else {
            result = new StartNoneEvent();
            StartNoneEvent startNoneEvent = (StartNoneEvent) result;

            BaseStartEventExecutionSet executionSet = startNoneEvent.getExecutionSet();
        }

        if (result instanceof hasCustomSLADueDate) {
            ((hasCustomSLADueDate) result).setSlaDueDate(v.getExtensionElements());
        }

        FallbackFlowNodeAdapter.unmarshal(result, v);

        return result;
    }

    @Override
    public FallbackStartEvent marshal(StartEvent v) {
        FallbackStartEvent fallbackStartEvent = new FallbackStartEvent();
        fallbackStartEvent.setId(v.getId());
        fallbackStartEvent.setName(v.getName());
        fallbackStartEvent.setDocumentation(v.getDocumentation());
        fallbackStartEvent.setExtensionElements(v.getExtensionElements());

        if (v instanceof StartCompensationEvent) {
            fallbackStartEvent.setEventDefinition(new CompensateEventDefinition());
        }
        if (v instanceof StartMessageEvent) {
            fallbackStartEvent.setEventDefinition(new MessageEventDefinition());
        }
        if (v instanceof StartSignalEvent) {
            fallbackStartEvent.setEventDefinition(new SignalEventDefinition());
        }
        if (v instanceof StartErrorEvent) {
            fallbackStartEvent.setEventDefinition(new ErrorEventDefinition());
        }
        if (v instanceof StartEscalationEvent) {
            fallbackStartEvent.setEventDefinition(new EscalationEventDefinition());
        }
        if (v instanceof StartConditionalEvent) {
            fallbackStartEvent.setEventDefinition(new ConditionalEventDefinition());
        }
        if (v instanceof StartTimerEvent) {
            fallbackStartEvent.setEventDefinition(new TimerEventDefinition());
        }

        if (v instanceof hasCustomSLADueDate) {
            ((hasCustomSLADueDate) v).setSlaDueDate(v.getExtensionElements());
        }
        return fallbackStartEvent;
    }
}