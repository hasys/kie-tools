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
import org.kie.workbench.common.stunner.bpmn.definition.hasCustomSLADueDate;
import org.kie.workbench.common.stunner.bpmn.definition.hasEscalationEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.hasOutputAssignments;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.escalation.InterruptingEscalationEventExecutionSet;
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
@Morph(base = StartEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "startEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class StartEscalationEvent extends StartEvent implements hasOutputAssignments,
                                                                hasEscalationEventDefinition,
                                                                hasCustomSLADueDate {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    private InterruptingEscalationEventExecutionSet executionSet;

    @Property
    @FormField(afterElement = "executionSet")
    @Valid
    @XmlTransient
    private DataIOSet dataIOSet;

    @XmlTransient
    private String escalationId;

    public EscalationEventDefinition escalationEventDefinition;

    @XmlElement(name = "dataOutput")
    @XmlUnwrappedCollection
    public List<DataOutput> dataOutputs;

    @XmlElement(name = "dataOutputAssociation")
    @XmlUnwrappedCollection
    public List<DataOutputAssociation> dataOutputAssociation;

    @XmlElement(name = "outputSet")
    @XmlUnwrappedCollection
    public List<OutputSet> outputSet;

    public StartEscalationEvent() {
        this("",
             "",
             new AdvancedData(),
             new DataIOSet(),
             new InterruptingEscalationEventExecutionSet());
    }

    public StartEscalationEvent(final @MapsTo("name") String name,
                                final @MapsTo("documentation") String documentation,
                                final @MapsTo("advancedData") AdvancedData advancedData,
                                final @MapsTo("dataIOSet") DataIOSet dataIOSet,
                                final @MapsTo("executionSet") InterruptingEscalationEventExecutionSet executionSet) {
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
        getExecutionSet().getEscalationRef().setValue(escalationEventDefinition.getEscalationRef());
        escalationId = escalationEventDefinition.getEsccode();
        this.escalationEventDefinition = escalationEventDefinition;
    }

    public Escalation getEscalation() {
        return hasEscalationEventDefinition.super.getEscalation();
    }

    public String getEscalationId() {
        return escalationId != null ? escalationId : escalationEventDefinition.getEsccode();
    }

    public void setEscalationId(String escalationId) {
        this.escalationId = escalationId;
    }

    public List<DataOutput> getDataOutputs() {
        return hasOutputAssignments.super.getDataOutputs();
    }

    public void setDataOutputs(List<DataOutput> dataOutputs) {
        this.dataOutputs = dataOutputs;
    }

    public List<DataOutputAssociation> getDataOutputAssociation() {
        return hasOutputAssignments.super.getDataOutputAssociation();
    }

    public void setDataOutputAssociation(List<DataOutputAssociation> dataOutputAssociation) {
        this.dataOutputAssociation = dataOutputAssociation;
    }

    public List<OutputSet> getOutputSet() {
        return hasOutputAssignments.super.getOutputSet();
    }

    public void setOutputSet(List<OutputSet> outputSets) {
        this.outputSet = outputSets;
    }

    public ExtensionElements getSuperExtensionElements() {
        return super.getExtensionElements();
    }

    public String getSlaDueDateString() {
        return executionSet.getSlaDueDate();
    }

    @Override
    public void setSlaDueDateString(String slaDueDate) {
        executionSet.setSlaDueDate(slaDueDate);
    }

    /*
     Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
     Variable is not used and always null. Getters/setters redirect data from other execution sets.
     Execution sets not removed due to how forms works now, should be refactored during the migration
     to the new forms.
      */
    @Override
    public ExtensionElements getExtensionElements() {
        return hasCustomSLADueDate.super.getExtensionElements();
    }

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
    Variable is not used and always null. Getters/setters redirect data from other execution sets.
    Execution sets not removed due to how forms works now, should be refactored during the migration
    to the new forms.
     */
    @Override
    public void setExtensionElements(ExtensionElements extensionElements) {
        super.setExtensionElements(extensionElements);
    }

    public InterruptingEscalationEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(InterruptingEscalationEventExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    public DataIOSet getDataIOSet() {
        return dataIOSet;
    }

    public void setDataIOSet(DataIOSet dataIOSet) {
        this.dataIOSet = dataIOSet;
    }

    @Override
    public boolean hasOutputVars() {
        return true;
    }

    @Override
    public boolean isSingleOutputVar() {
        return true;
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
        if (o instanceof StartEscalationEvent) {
            StartEscalationEvent other = (StartEscalationEvent) o;
            return super.equals(other) &&
                    Objects.equals(executionSet, other.executionSet) &&
                    Objects.equals(dataIOSet, other.dataIOSet);
        }
        return false;
    }
}
