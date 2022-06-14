/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlCData;

import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.FlowNode;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.BPMNGeneralSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.Documentation;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.Name;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.util.HashUtil;

/**
 * Marker interface for all BPMN definitions like nodes or artifacts, connections.
 */
public abstract class FlowElement implements FlowElementInterface {

    @Property
    @FormField
    @Valid
    protected BPMNGeneralSet general = new BPMNGeneralSet();

    @XmlAttribute
    protected String name;

    @XmlCData
    @XmlAttribute(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
    protected String documentation;

    @XmlAttribute
    private String id;

    public FlowElement() {
    }

    public FlowElement(final String name, final String documentation) {
        general.setName(new Name(name));
        general.setDocumentation(new Documentation(documentation));
    }

    public FlowElement(final String id, final String name, final String documentation) {
        this.id = id;
        general.setName(new Name(name));
        general.setDocumentation(new Documentation(documentation));
    }

    @Override
    public String getName() {
        return general.getName().getValue();
    }

    public void setName(String name) {
        general.setName(new Name(name));
    }

    @Override
    public String getDocumentation() {
        return general.getDocumentation().getValue();
    }

    public void setDocumentation(String documentation) {
        general.setDocumentation(new Documentation(documentation));
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BPMNGeneralSet getGeneral() {
        return general;
    }

    public void setGeneral(BPMNGeneralSet general) {
        this.general = general;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlowNode)) {
            return false;
        }
        FlowNode flowNode = (FlowNode) o;
        return Objects.equals(getGeneral(), flowNode.getGeneral())
                && Objects.equals(getId(), flowNode.getId());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(getClass()),
                                         Objects.hashCode(getId()),
                                         Objects.hashCode(getGeneral()));
    }
}