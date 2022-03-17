/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.kie.workbench.common.stunner.core.util.HashUtil;

@XmlRootElement(name = "linkEventDefinition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class TimerEventDefinition {

    @XmlAttribute
    private TimeDuration timeDuration;

    @XmlAttribute
    private TimeCycle timeCycle;

    @XmlAttribute
    private TimeDate timeDate;

    public TimerEventDefinition() {

    }

    public TimeDuration getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(TimeDuration timeDuration) {
        this.timeDuration = timeDuration;
    }

    public TimeCycle getTimeCycle() {
        return timeCycle;
    }

    public void setTimeCycle(TimeCycle timeCycle) {
        this.timeCycle = timeCycle;
    }

    public TimeDate getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(TimeDate timeDate) {
        this.timeDate = timeDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimerEventDefinition)) {
            return false;
        }
        TimerEventDefinition that = (TimerEventDefinition) o;
        return Objects.equals(getTimeDuration(), that.getTimeDuration())
                && Objects.equals(getTimeCycle(), that.getTimeCycle())
                && Objects.equals(getTimeDate(), that.getTimeDate());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(timeDuration),
                                         Objects.hashCode(timeCycle),
                                         Objects.hashCode(timeDate));
    }
}
