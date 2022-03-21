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

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlAttribute;
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
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOModel;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.BaseSubprocessTaskExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.CalledElement;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.Independent;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.ReusableSubprocessTaskExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.WaitForCompletion;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.rule.annotation.CanDock;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@Morph(base = BaseNonContainerSubprocess.class)
@CanDock(roles = {"IntermediateEventOnSubprocessBoundary"})
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "callActivity", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class ReusableSubprocess
        extends BaseNonContainerSubprocess implements DataIOModel,
                                                      IsMultipleInstance {

    @Property
    @FormField(
            afterElement = "documentation"
    )
    @Valid
    @XmlTransient
    protected ReusableSubprocessTaskExecutionSet executionSet;

    @Property
    @FormField(
            afterElement = "executionSet"
    )
    @Valid
    @XmlTransient
    protected DataIOSet dataIOSet;

    /**
     * Always null, getter and setter works with forms values from executionSet
     */
    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private Boolean independent;

    /**
     * Always null, getter and setter works with forms values from executionSet
     */
    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private Boolean waitForCompletion;

    /**
     * Always null, getter and setter works with forms values from executionSet
     */
    @XmlAttribute
    private String calledElement;

    // Used to easier creation of ItemDefinitions in Definitions root node
    @XmlTransient
    private List<ItemDefinition> itemDefinitions = new ArrayList<>();

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

    public ReusableSubprocess() {
        this("Sub-process",
             "",
             new ReusableSubprocessTaskExecutionSet(),
             new DataIOSet(),
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(),
             new SimulationSet(),
             new AdvancedData());
    }

    @Override
    public ReusableSubprocessTaskExecutionSet getExecutionSet() {
        return executionSet;
    }

    @Override
    public void setExecutionSet(BaseSubprocessTaskExecutionSet executionSet) {
        this.executionSet = (ReusableSubprocessTaskExecutionSet) executionSet;
    }

    public ReusableSubprocess(final @MapsTo("name") String name,
                              final @MapsTo("documentation") String documentation,
                              final @MapsTo("executionSet") ReusableSubprocessTaskExecutionSet executionSet,
                              final @MapsTo("dataIOSet") DataIOSet dataIOSet,
                              final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                              final @MapsTo("fontSet") FontSet fontSet,
                              final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                              final @MapsTo("simulationSet") SimulationSet simulationSet,
                              final @MapsTo("advancedData") AdvancedData advancedData) {
        super(name,
              documentation,
              backgroundSet,
              fontSet,
              dimensionsSet,
              simulationSet,
              advancedData);
        this.executionSet = executionSet;
        this.dataIOSet = dataIOSet;
    }

    @Override
    public boolean hasInputVars() {
        return true;
    }

    @Override
    public boolean isSingleInputVar() {
        return false;
    }

    @Override
    public boolean hasOutputVars() {
        return true;
    }

    @Override
    public boolean isSingleOutputVar() {
        return false;
    }

    @Override
    public DataIOSet getDataIOSet() {
        return dataIOSet;
    }

    @Override
    public void setDataIOSet(final DataIOSet dataIOSet) {
        this.dataIOSet = dataIOSet;
    }

    public Boolean getIndependent() {
        return executionSet.getIndependent().getValue();
    }

    public void setIndependent(Boolean independent) {
        executionSet.setIndependent(new Independent(independent));
    }

    public Boolean getWaitForCompletion() {
        return executionSet.getWaitForCompletion().getValue();
    }

    public void setWaitForCompletion(Boolean waitForCompletion) {
        executionSet.setWaitForCompletion(new WaitForCompletion(waitForCompletion));
    }

    public String getCalledElement() {
        return executionSet.getCalledElement().getValue();
    }

    public void setCalledElement(String calledElement) {
        executionSet.setCalledElement(new CalledElement(calledElement));
    }

    @Override
    public ExtensionElements getExtensionElements() {
        ExtensionElements elements = super.getExtensionElements();

        if (executionSet.getAbortParent().getValue() != null && !executionSet.getAbortParent().getValue()) {
            MetaData abortParent = new MetaData("customAbortParent",
                                                executionSet.getAbortParent().getValue().toString());
            elements.getMetaData().add(abortParent);
        }

        return elements.getMetaData().isEmpty() ? null : elements;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode(),
                                         dataIOSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ReusableSubprocess) {
            ReusableSubprocess other = (ReusableSubprocess) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet) &&
                    dataIOSet.equals(other.dataIOSet);
        }
        return false;
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
    public IoSpecification getIoSpecification() {
        if (getId() == null) {
            return null;
        }
        ioSpecification = IsMultipleInstance.super.getIoSpecification();

        if (!dataIOSet.getAssignmentsinfo().getValue().isEmpty()) {
            String wholeAssignments = dataIOSet.getAssignmentsinfo().getValue();

            List<DataInput> dataInputs = AssignmentParser.parseDataInputs(getId(), wholeAssignments);
            for (DataInput dataInput : dataInputs) {
                ioSpecification.getDataInput().add(dataInput);
                ioSpecification.getInputSet().getDataInputRefs().add(new DataInputRefs(dataInput.getId()));
            }

            List<DataOutput> dataOutputs = AssignmentParser.parseDataOutputs(getId(), wholeAssignments);
            for (DataOutput dataOutput : dataOutputs) {
                ioSpecification.getDataOutput().add(dataOutput);
                ioSpecification.getOutputSet().getDataOutputRefs().add(new DataOutputRefs(dataOutput.getId()));
            }
        }

        return ioSpecification;
    }

    public void setIoSpecification(IoSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    @Override
    public List<DataInputAssociation> getDataInputAssociation() {
        dataInputAssociation = IsMultipleInstance.super.getDataInputAssociation();

        // Can happen on designer startup during CDI proxies creation phase
        if (getId() != null) {
            if (!dataIOSet.getAssignmentsinfo().getValue().isEmpty()) {
                dataInputAssociation.addAll(AssignmentParser.parseDataInputAssociation(getId(),
                                                                                       dataIOSet.getAssignmentsinfo().getValue()));
            }
        }
        return dataInputAssociation;
    }

    public void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation) {
    }

    @Override
    public List<DataOutputAssociation> getDataOutputAssociation() {
        dataOutputAssociation = IsMultipleInstance.super.getDataOutputAssociation();

        if (!dataIOSet.getAssignmentsinfo().getValue().isEmpty()) {
            dataOutputAssociation.addAll(AssignmentParser.parseDataOutputAssociation(getId(),
                                                                                     dataIOSet.getAssignmentsinfo().getValue()));
        }

        return dataOutputAssociation;
    }

    public void setDataOutputAssociation(List<DataOutputAssociation> dataOutputAssociation) {

    }

    @Override
    public MultiInstanceLoopCharacteristics getMultiInstanceLoopCharacteristics() {
        return IsMultipleInstance.super.getMultiInstanceLoopCharacteristics();
    }

    public void setMultiInstanceLoopCharacteristics(MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {

    }
}
