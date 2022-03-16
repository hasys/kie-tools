/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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
import org.kie.workbench.common.stunner.bpmn.definition.hasErrorEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.hasOutputAssignments;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.error.InterruptingErrorEventExecutionSet;
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
@Morph(base = StartEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "startEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class StartErrorEvent extends StartEvent implements hasOutputAssignments,
                                                           hasErrorEventDefinition, hasCustomSLADueDate {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    protected InterruptingErrorEventExecutionSet executionSet;

    @Property
    @FormField(afterElement = "executionSet")
    @Valid
    @XmlTransient
    protected DataIOSet dataIOSet;

    @XmlTransient
    private String errorId;

    public ErrorEventDefinition errorEventDefinition;

    @XmlElement(name = "dataOutput")
    @XmlUnwrappedCollection
    public List<DataOutput> dataOutputs;

    @XmlElement(name = "dataOutputAssociation")
    @XmlUnwrappedCollection
    public List<DataOutputAssociation> dataOutputAssociation;

    @XmlElement(name = "outputSet")
    @XmlUnwrappedCollection
    public List<OutputSet> outputSet;

    public StartErrorEvent() {
        this("",
             "",
             new AdvancedData(),
             new DataIOSet(),
             new InterruptingErrorEventExecutionSet());
    }

    public StartErrorEvent(final @MapsTo("name") String name,
                           final @MapsTo("documentation") String documentation,
                           final @MapsTo("advancedData") AdvancedData advancedData,
                           final @MapsTo("dataIOSet") DataIOSet dataIOSet,
                           final @MapsTo("executionSet") InterruptingErrorEventExecutionSet executionSet) {
        super(name,
              documentation,
              advancedData);
        this.dataIOSet = dataIOSet;
        this.executionSet = executionSet;
    }

    public String getErrorRefValue() {
        return executionSet.getErrorRef().getValue();
    }
    public ErrorEventDefinition getErrorEventDefinition() {
        return hasErrorEventDefinition.super.getErrorEventDefinition();
    }

    public void setErrorEventDefinition(ErrorEventDefinition errorEventDefinition) {
        this.errorEventDefinition = errorEventDefinition;
    }

    public ErrorRef getError() {
        return hasErrorEventDefinition.super.getError();
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
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

    public InterruptingErrorEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(final InterruptingErrorEventExecutionSet executionSet) {
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
                                         dataIOSet.hashCode(),
                                         executionSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StartErrorEvent) {
            StartErrorEvent other = (StartErrorEvent) o;
            return super.equals(other) &&
                    dataIOSet.equals(other.dataIOSet) &&
                    executionSet.equals(other.executionSet);
        }
        return false;
    }
}
