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

@XmlRootElement(name = "escalationEventDefinition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class EscalationEventDefinition extends AbstractEventDefinition {

    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private String escode;

    @XmlAttribute
    private String escalationRef;

    public EscalationEventDefinition() {

    }

    public EscalationEventDefinition(String droolsEsCode, String escalationRef) {
        this.escode = droolsEsCode;
        this.escalationRef = escalationRef;
    }

    public String getEscode() {
        return escode;
    }

    public void setEscode(String escode) {
        this.escode = escode;
    }

    public void setEscalationRef(String escalationRef) {
        this.escode = escalationRef;
    }

    public String getEscalationRef() {
        return escalationRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EscalationEventDefinition)) {
            return false;
        }
        EscalationEventDefinition that = (EscalationEventDefinition) o;
        return Objects.equals(getEscode(), that.getEscode())
                && Objects.equals(getEscalationRef(), that.getEscalationRef());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(escode),
                                         Objects.hashCode(escode));
    }
}
