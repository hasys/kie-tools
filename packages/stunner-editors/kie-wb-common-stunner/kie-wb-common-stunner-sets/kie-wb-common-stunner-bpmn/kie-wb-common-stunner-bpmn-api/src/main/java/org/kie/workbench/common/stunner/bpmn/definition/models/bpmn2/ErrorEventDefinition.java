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

@XmlRootElement(name = "errorEventDefinition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class ErrorEventDefinition {

    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private String errorref;

    @XmlAttribute
    private String errorRef;

    public ErrorEventDefinition() {

    }

    public ErrorEventDefinition(String droolsErrorName, String errorId) {
        this.errorref = droolsErrorName;
        this.errorRef = errorId;
    }

    public String getErrorref() {
        return errorref;
    }

    public void setErrorRef(String errorRef) {
        this.errorref = errorRef;
    }

    public String getErrorRef() {
        return errorRef;
    }

    public void setErrorref(String errorRef) {
        this.errorRef = errorRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorEventDefinition)) {
            return false;
        }
        ErrorEventDefinition that = (ErrorEventDefinition) o;
        return Objects.equals(getErrorref(), that.getErrorref())
                && Objects.equals(getErrorRef(), that.getErrorRef());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(errorref),
                                         Objects.hashCode(errorref));
    }
}
