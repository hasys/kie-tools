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
package org.kie.workbench.common.stunner.bpmn.definition;

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.TimeCycle;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.TimeDate;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.TimeDuration;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.TimerEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.timer.TimerSettingsValue;

public interface hasTimerEventDefinition {

    TimerSettingsValue getTimerSettingsValue();

    void setTimerEventDefinition(TimerEventDefinition timerEventDefinition);

    default TimerEventDefinition getTimerEventDefinition() {
        TimerEventDefinition timerEventDefinition = null;
        if (getTimerSettingsValue().getTimeDuration() != null) {
            timerEventDefinition = new TimerEventDefinition();
            TimeDuration timeDuration = new TimeDuration("bpmn2:tFormalExpression", getTimerSettingsValue().getTimeDuration());
            timerEventDefinition.setTimeDuration(timeDuration);
        } else if (getTimerSettingsValue().getTimeCycle() != null) {
            timerEventDefinition = new TimerEventDefinition();
            TimeCycle timeCycle = new TimeCycle(getTimerSettingsValue().getTimeCycleLanguage(), getTimerSettingsValue().getTimeCycle() );
            timerEventDefinition.setTimeCycle(timeCycle);
        } else if (getTimerSettingsValue().getTimeDate() != null) {
            timerEventDefinition = new TimerEventDefinition();
            TimeDate timeDate = new TimeDate("bpmn2:tFormalExpression", getTimerSettingsValue().getTimeDate());
            timerEventDefinition.setTimeDate(timeDate);
        }
        return timerEventDefinition;
    }
}
