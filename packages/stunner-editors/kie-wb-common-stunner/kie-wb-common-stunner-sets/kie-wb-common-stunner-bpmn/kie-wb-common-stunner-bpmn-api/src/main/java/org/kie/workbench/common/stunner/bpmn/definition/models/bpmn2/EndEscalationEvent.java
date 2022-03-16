/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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
import org.kie.workbench.common.stunner.bpmn.definition.hasEscalationEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.hasInputAssignments;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.escalation.EscalationEventExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.FIELD_CONTAINER_PARAM;

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
public class EndEscalationEvent extends EndEvent implements hasInputAssignments,
                                                            hasEscalationEventDefinition {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    private EscalationEventExecutionSet executionSet;

    @Property
    @FormField(afterElement = "executionSet")
    @XmlTransient
    private DataIOSet dataIOSet;

    @XmlTransient
    private String escalationId;

    public EscalationEventDefinition escalationEventDefinition;

    @XmlElement(name = "dataInput")
    @XmlUnwrappedCollection
    public List<DataInput> dataInputs;

    @XmlElement(name = "dataInputAssociation")
    @XmlUnwrappedCollection
    public List<DataInputAssociation> dataInputAssociation;

    @XmlElement(name = "inputSet")
    @XmlUnwrappedCollection
    public List<InputSet> inputSet;

    public EndEscalationEvent() {
        this("",
             "",
             new EscalationEventExecutionSet(),
             new AdvancedData(),
             new DataIOSet());
    }

    public EndEscalationEvent(final @MapsTo("name") String name,
                              final @MapsTo("documentation") String documentation,
                              final @MapsTo("executionSet") EscalationEventExecutionSet executionSet,
                              final @MapsTo("advancedData") AdvancedData advancedData,
                              final @MapsTo("dataIOSet") DataIOSet dataIOSet) {
        super(name,
              documentation,
              advancedData);
        this.executionSet = executionSet;
        this.dataIOSet = dataIOSet;
    }

    public String getEscalationRefValue() {
        return getExecutionSet().getEscalationRef().getValue();
    }

    public EscalationEventDefinition getEscalationEventDefinition() {
        return hasEscalationEventDefinition.super.getEscalationEventDefinition();
    }

    public void setEscalationEventDefinition(EscalationEventDefinition escalationEventDefinition) {
        this.escalationEventDefinition = escalationEventDefinition;
    }

    public Escalation getEscalation() {
        return hasEscalationEventDefinition.super.getEscalation();
    }

    public String getEscalationId() {
        return escalationId;
    }

    public void setEscalationId(String escalationId) {
        this.escalationId = escalationId;
    }

    public List<DataInput> getDataInputs() {
        return hasInputAssignments.super.getDataInputs();
    }

    public void setDataInputs(List<DataInput> dataInputs) {
        this.dataInputs = dataInputs;
    }

    public List<DataInputAssociation> getDataInputAssociation() {
        return hasInputAssignments.super.getDataInputAssociation();
    }

    public void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation) {
        this.dataInputAssociation = dataInputAssociation;
    }

    public List<InputSet> getInputSet() {
        return hasInputAssignments.super.getInputSet();
    }

    public void setInputSet(List<InputSet> inputSets) {
        this.inputSet = inputSets;
    }

    @Override
    public boolean hasInputVars() {
        return true;
    }

    @Override
    public boolean isSingleInputVar() {
        return true;
    }

    public EscalationEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(EscalationEventExecutionSet executionSet) {
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
                                         Objects.hashCode(executionSet),
                                         Objects.hashCode(dataIOSet));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof EndEscalationEvent) {
            EndEscalationEvent other = (EndEscalationEvent) o;
            return super.equals(other) &&
                    Objects.equals(executionSet, other.executionSet) &&
                    Objects.equals(dataIOSet, other.dataIOSet);
        }
        return false;
    }
}
