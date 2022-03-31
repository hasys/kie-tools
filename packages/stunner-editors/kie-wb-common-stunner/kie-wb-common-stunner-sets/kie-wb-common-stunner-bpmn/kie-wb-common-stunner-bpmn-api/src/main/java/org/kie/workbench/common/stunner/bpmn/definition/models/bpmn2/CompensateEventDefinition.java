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

/***
 * Used only for marshalling/unmarshalling purposes as empty element compensateEventDefinition
 */
@XmlRootElement(name = "compensateEventDefinition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class CompensateEventDefinition {

    @XmlAttribute(name = "activityRef")
    private String activityRef;

    public CompensateEventDefinition() {
    }

    public CompensateEventDefinition(String activityRef) {
        this.activityRef = activityRef;
    }

    public String getActivityRef() {
        return activityRef;
    }

    public void setActivityRef(String activityRef) {
        this.activityRef = activityRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompensateEventDefinition)) {
            return false;
        }
        CompensateEventDefinition that = (CompensateEventDefinition) o;
        return Objects.equals(getActivityRef(), that.getActivityRef());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(getActivityRef()));
    }
}
