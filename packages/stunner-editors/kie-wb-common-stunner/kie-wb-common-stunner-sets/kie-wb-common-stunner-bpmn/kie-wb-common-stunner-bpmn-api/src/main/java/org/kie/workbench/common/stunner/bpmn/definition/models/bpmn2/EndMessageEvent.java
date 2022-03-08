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

package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FieldParam;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.forms.adf.definitions.settings.FieldPolicy;
import org.kie.workbench.common.stunner.bpmn.definition.hasInputAssignments;
import org.kie.workbench.common.stunner.bpmn.definition.hasMessageEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.message.MessageEventExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@Morph(base = EndEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "endEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class EndMessageEvent extends EndEvent implements hasMessageEventDefinition,
                                                         hasInputAssignments {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    protected MessageEventExecutionSet executionSet;

    @Property
    @FormField(afterElement = "executionSet")
    @XmlTransient
    protected DataIOSet dataIOSet;

    @XmlTransient
    private String messageId;

    public MessageEventDefinition messageEventDefinition;

    @XmlElement(name = "dataInput")
    @XmlUnwrappedCollection
    public List<DataInput> dataInputs;

    @XmlElement(name = "dataInputAssociation")
    @XmlUnwrappedCollection
    public List<DataInputAssociation> dataInputAssociation;

    @XmlElement(name = "inputSet")
    @XmlUnwrappedCollection
    public List<InputSet> inputSet;

    public EndMessageEvent() {
        this("",
             "",
             new AdvancedData(),
             new MessageEventExecutionSet(),
             new DataIOSet());
    }

    public EndMessageEvent(final @MapsTo("name") String name,
                           final @MapsTo("documentation") String documentation,
                           final @MapsTo("advancedData") AdvancedData advancedData,
                           final @MapsTo("executionSet") MessageEventExecutionSet executionSet,
                           final @MapsTo("dataIOSet") DataIOSet dataIOSet) {
        super(name,
              documentation,
              advancedData);
        this.executionSet = executionSet;
        this.dataIOSet = dataIOSet;

        labels.add("messageflow_start");
    }

    public void setMessageEventDefinition(MessageEventDefinition messageEventDefinition) {
        this.messageEventDefinition = messageEventDefinition;
    }

    public Message getMessage() {
        return new Message(getMessageId(),
                           executionSet.getMessageRef().getValue(),
                           executionSet.getMessageRef().getValue() + "Type");
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public List<DataInput> getDataInputs() {
        return AssignmentParser.parseDataInputs(getId(), dataIOSet.getAssignmentsinfo().getValue());
    }

    public void setDataInputs(List<DataInput> dataInputs) {
        this.dataInputs = dataInputs;
    }

    public void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation) {
        this.dataInputAssociation = dataInputAssociation;
    }

    public void setInputSet(List<InputSet> inputSet) {
        this.inputSet = inputSet;
    }

    @Override
    public boolean hasInputVars() {
        return true;
    }

    @Override
    public boolean isSingleInputVar() {
        return true;
    }

    public MessageEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(MessageEventExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    public DataIOSet getDataIOSet() {
        return dataIOSet;
    }

    public void setDataIOSet(DataIOSet dataIOSet) {
        this.dataIOSet = dataIOSet;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode(),
                                         dataIOSet.hashCode(),
                                         labels.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EndMessageEvent) {
            EndMessageEvent other = (EndMessageEvent) o;
            return super.equals(other) &&
                    Objects.equals(executionSet, other.executionSet) &&
                    Objects.equals(dataIOSet, other.dataIOSet) &&
                    Objects.equals(labels, other.labels);
        }
        return false;
    }
}
