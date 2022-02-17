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

import java.util.ArrayList;
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
import org.kie.workbench.common.stunner.bpmn.definition.IsMultipleInstance;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.BaseSubprocessTaskExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.MultipleInstanceSubprocessTaskExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.HasProcessData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.ProcessData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.rule.annotation.CanContain;
import org.kie.workbench.common.stunner.core.rule.annotation.CanDock;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@CanContain(roles = {"all"})
@CanDock(roles = {"IntermediateEventOnSubprocessBoundary"})
@Definition
@Morph(base = BaseSubprocess.class)

@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "subProcess", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class MultipleInstanceSubprocess extends ConnectedBaseSubprocess implements HasProcessData<ProcessData>,
                                                                                   IsMultipleInstance {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    private MultipleInstanceSubprocessTaskExecutionSet executionSet;

    @XmlElement
    private IoSpecification ioSpecification;

    @XmlElement(name = "dataInputAssociation")
    @XmlUnwrappedCollection
    private List<DataInputAssociation> dataInputAssociation = new ArrayList<>();

    @XmlElement(name = "dataOutputAssociation")
    @XmlUnwrappedCollection
    private List<DataOutputAssociation> dataOutputAssociation = new ArrayList<>();

    @XmlElement(name = "multiInstanceLoopCharacteristics")
    private MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics;

    public MultipleInstanceSubprocess() {
        this("Multiple Instance Sub-process",
             "",
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(),
             new SimulationSet(),
             new MultipleInstanceSubprocessTaskExecutionSet(),
             new ProcessData(),
             new AdvancedData());
    }

    public MultipleInstanceSubprocess(final @MapsTo("name") String name,
                                      final @MapsTo("documentation") String documentation,
                                      final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                                      final @MapsTo("fontSet") FontSet fontSet,
                                      final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                                      final @MapsTo("simulationSet") SimulationSet simulationSet,
                                      final @MapsTo("executionSet") MultipleInstanceSubprocessTaskExecutionSet executionSet,
                                      final @MapsTo("processData") ProcessData processData,
                                      final @MapsTo("advancedData") AdvancedData advancedData) {
        super(name,
              documentation,
              backgroundSet,
              fontSet,
              dimensionsSet,
              simulationSet,
              processData,
              advancedData);
        this.executionSet = executionSet;
    }

    @Override
    public MultipleInstanceSubprocessTaskExecutionSet getExecutionSet() {
        return executionSet;
    }

    @Override
    public void setExecutionSet(BaseSubprocessTaskExecutionSet executionSet) {
        this.executionSet = (MultipleInstanceSubprocessTaskExecutionSet) executionSet;
    }

    public void setIoSpecification(IoSpecification ioSpecification) {

    }

    public void setDataInputAssociation(List<DataInputAssociation> associations) {

    }

    public void setDataOutputAssociation(List<DataOutputAssociation> associations) {

    }

    public void setMultiInstanceLoopCharacteristics(MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        this.multiInstanceLoopCharacteristics = multiInstanceLoopCharacteristics;
    }

    @Override
    public boolean isMultipleInstance() {
        return executionSet.getIsMultipleInstance().getValue();
    }

    @Override
    public String getMultipleInstanceCollectionInputName() {
        return executionSet.getMultipleInstanceCollectionInput().getValue();
    }

    @Override
    public String getMultipleInstanceDataInputName() {
        return executionSet.getMultipleInstanceDataInput().getValue().split(":")[0];
    }

    @Override
    public String getMultipleInstanceCollectionOutputName() {
        return executionSet.getMultipleInstanceCollectionOutput().getValue();
    }

    @Override
    public String getMultipleInstanceDataOutputName() {
        return executionSet.getMultipleInstanceDataOutput().getValue().split(":")[0];
    }

    @Override
    public String getMultipleInstanceCompletionCondition() {
        return executionSet.getMultipleInstanceCompletionCondition().getValue();
    }

    @Override
    public MultiInstanceLoopCharacteristics getMultiInstanceLoopCharacteristics() {
        return IsMultipleInstance.super.getMultiInstanceLoopCharacteristics();
    }

    @Override
    public IoSpecification getIoSpecification() {
        return IsMultipleInstance.super.getIoSpecification();
    }

    @Override
    public List<DataInputAssociation> getDataInputAssociation() {
        return IsMultipleInstance.super.getDataInputAssociation();
    }

    @Override
    public List<DataOutputAssociation> getDataOutputAssociation() {
        return IsMultipleInstance.super.getDataOutputAssociation();
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         Objects.hashCode(labels),
                                         Objects.hashCode(executionSet));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MultipleInstanceSubprocess) {
            MultipleInstanceSubprocess other = (MultipleInstanceSubprocess) o;
            return super.equals(other)
                    && Objects.equals(this.labels, other.labels)
                    && Objects.equals(this.executionSet, other.executionSet);
        }
        return false;
    }
}