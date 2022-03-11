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
import org.kie.workbench.common.stunner.bpmn.definition.hasOutputAssignments;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.CircleDimensionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.error.CancellingErrorEventExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.kie.workbench.common.stunner.core.util.StringUtils;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@Morph(base = BaseCatchingIntermediateEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "intermediateCatchEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class IntermediateErrorEventCatching extends BaseCatchingIntermediateEvent implements hasOutputAssignments {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    protected CancellingErrorEventExecutionSet executionSet;

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

    public IntermediateErrorEventCatching() {
        this("",
             "",
             new BackgroundSet(),
             new FontSet(),
             new CircleDimensionSet(),
             new DataIOSet(),
             new AdvancedData(),
             new CancellingErrorEventExecutionSet());
    }

    public IntermediateErrorEventCatching(final @MapsTo("name") String name,
                                          final @MapsTo("documentation") String documentation,
                                          final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                                          final @MapsTo("fontSet") FontSet fontSet,
                                          final @MapsTo("dimensionsSet") CircleDimensionSet dimensionsSet,
                                          final @MapsTo("dataIOSet") DataIOSet dataIOSet,
                                          final @MapsTo("advancedData") AdvancedData advancedData,
                                          final @MapsTo("executionSet") CancellingErrorEventExecutionSet executionSet) {
        super(name,
              documentation,
              backgroundSet,
              fontSet,
              dimensionsSet,
              dataIOSet,
              advancedData);
        this.executionSet = executionSet;
    }

    @Override
    protected void initLabels() {
        super.initLabels();
        labels.remove("sequence_end");
    }

    public ErrorEventDefinition getErrorEventDefinition() {
        return new ErrorEventDefinition(executionSet.getErrorRef().getValue(), errorId);
    }

    public void setErrorEventDefinition(ErrorEventDefinition errorEventDefinition) {
        this.errorEventDefinition = errorEventDefinition;
    }

    public ErrorRef getError() {
        return new ErrorRef(getErrorId(),
                            executionSet.getErrorRef().getValue());
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    public void setDataOutputs(List<DataOutput> dataOutputs) {
        this.dataOutputs = dataOutputs;
    }

    public void setDataOutputAssociation(List<DataOutputAssociation> dataOutputAssociation) {
        this.dataOutputAssociation = dataOutputAssociation;
    }

    public void setOutputSet(List<OutputSet> outputSet) {
        this.outputSet = outputSet;
    }

    /*
     Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
     Variable is not used and always null. Getters/setters redirect data from other execution sets.
     Execution sets not removed due to how forms works now, should be refactored during the migration
     to the new forms.
      */
    @Override
    public ExtensionElements getExtensionElements() {
        ExtensionElements elements = super.getExtensionElements();
        if (StringUtils.nonEmpty(this.getExecutionSet().getSlaDueDate().getValue())) {
            MetaData sla = new MetaData("customSLADueDate", this.getExecutionSet().getSlaDueDate().getValue());
            elements.getMetaData().add(sla);
        }

        return elements.getMetaData().isEmpty() ? null : elements;
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

    public CancellingErrorEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(CancellingErrorEventExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntermediateErrorEventCatching) {
            IntermediateErrorEventCatching other = (IntermediateErrorEventCatching) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet);
        }
        return false;
    }
}