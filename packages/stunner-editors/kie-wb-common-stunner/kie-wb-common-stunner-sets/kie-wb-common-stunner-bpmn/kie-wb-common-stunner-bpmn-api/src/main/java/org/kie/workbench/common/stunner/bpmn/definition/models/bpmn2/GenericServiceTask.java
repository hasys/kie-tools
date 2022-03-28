/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.OnEntryScript;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.OnExitScript;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOModel;
import org.kie.workbench.common.stunner.bpmn.definition.property.service.GenericServiceTaskExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.ScriptTypeValue;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskType;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskTypes;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.rule.annotation.CanDock;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@CanDock(roles = {"IntermediateEventOnActivityBoundary"})
@Morph(base = BaseTask.class)
@FormDefinition(
        policy = FieldPolicy.ONLY_MARKED,
        startElement = "general",
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "serviceTask", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class GenericServiceTask extends BaseTask implements DataIOModel,
                                                            IsMultipleInstance {

    @Property
    @FormField(
            afterElement = "general"
    )
    @XmlTransient
    protected GenericServiceTaskExecutionSet executionSet;

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

    @XmlAttribute(name = "serviceimplementation", namespace = "http://www.jboss.org/drools")
    private String serviceImplementation;

    @XmlAttribute(name = "serviceinterface", namespace = "http://www.jboss.org/drools")
    private String serviceInterface;

    @XmlAttribute(name = "serviceoperation", namespace = "http://www.jboss.org/drools")
    private String serviceOperation;

    @XmlAttribute
    private String implementation;

    @XmlAttribute
    private String operationRef;

    public GenericServiceTask() {
        this("Service Task",
             "",
             new GenericServiceTaskExecutionSet(),
             new SimulationSet(),
             new TaskType(TaskTypes.SERVICE_TASK),
             new AdvancedData());
    }

    public GenericServiceTask(final @MapsTo("name") String name,
                              final @MapsTo("documentation") String documentation,
                              final @MapsTo("executionSet") GenericServiceTaskExecutionSet executionSet,
                              final @MapsTo("simulationSet") SimulationSet simulationSet,
                              final @MapsTo("taskType") TaskType taskType,
                              final @MapsTo("advancedData") AdvancedData advancedData) {
        super(name,
              documentation,
              simulationSet,
              taskType,
              advancedData);
        this.executionSet = executionSet;
    }

    public GenericServiceTaskExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(final GenericServiceTaskExecutionSet executionSet) {
        this.executionSet = executionSet;
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
    public ExtensionElements getExtensionElements() {
        ExtensionElements elements = super.getExtensionElements();
        if (elements == null) {
            elements = new ExtensionElements();
        }

        List<MetaData> metaData = elements.getMetaData();
        metaData = metaData != null ? metaData : new ArrayList<>();
        if (executionSet.getIsAsync().getValue()) {
            MetaData customAutoStart = new MetaData("customAsync",
                                                    "true");
            metaData.add(customAutoStart);
        }

        if (executionSet.getAdHocAutostart().getValue()) {
            MetaData customAutoStart = new MetaData("customAutoStart",
                                                    "true");
            metaData.add(customAutoStart);
        }

        if (executionSet.getSlaDueDate().getValue() != null && !executionSet.getSlaDueDate().getValue().isEmpty()) {
            MetaData customAutoStart = new MetaData("customSLADueDate",
                                                    executionSet.getSlaDueDate().getValue());
            metaData.add(customAutoStart);
        }
        elements.setMetaData(metaData);

        if (!executionSet.getOnEntryAction().getValue().isEmpty()
                && !executionSet.getOnEntryAction().getValue().getValues().isEmpty()
                && !executionSet.getOnEntryAction().getValue().getValues().get(0).getScript().isEmpty()) {
            ScriptTypeValue value = executionSet.getOnEntryAction().getValue().getValues().get(0);
            OnEntryScript entryScript = new OnEntryScript(value.getLanguage(), value.getScript());
            elements.setOnEntryScript(entryScript);
        }

        if (!executionSet.getOnExitAction().getValue().isEmpty()
                && !executionSet.getOnExitAction().getValue().getValues().isEmpty()
                && !executionSet.getOnExitAction().getValue().getValues().get(0).getScript().isEmpty()) {
            ScriptTypeValue value = executionSet.getOnExitAction().getValue().getValues().get(0);
            OnExitScript exitScript = new OnExitScript(value.getLanguage(), value.getScript());
            elements.setOnExitScript(exitScript);
        }

        return elements.getMetaData().isEmpty() ? null : elements;
    }

    public List<ItemDefinition> getItemDefinitions() {
        itemDefinitions = new ArrayList<>();

        if (executionSet.getAssignmentsinfo() != null
                && executionSet.getAssignmentsinfo().getValue() != null
                && !executionSet.getAssignmentsinfo().getValue().isEmpty()) {
            itemDefinitions = new ArrayList<>();
            String wholeAssignments = executionSet.getAssignmentsinfo().getValue();
            List<ItemDefinition> inputDefinitions = AssignmentParser.getInputItemDefinitions(getId(), wholeAssignments);
            itemDefinitions.addAll(inputDefinitions);

            List<ItemDefinition> outputDefinitions = AssignmentParser.getOutputItemDefinitions(getId(), wholeAssignments);
            itemDefinitions.addAll(outputDefinitions);
        }

        return itemDefinitions;
    }

    public void setItemDefinitions(List<ItemDefinition> itemDefinitions) {
        this.itemDefinitions = itemDefinitions;
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

        if (!executionSet.getAssignmentsinfo().getValue().isEmpty()) {
            String wholeAssignments = executionSet.getAssignmentsinfo().getValue();

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

    @Override
    public List<DataInputAssociation> getDataInputAssociation() {
        dataInputAssociation = IsMultipleInstance.super.getDataInputAssociation();

        // Can happen on designer startup during CDI proxies creation phase
        if (getId() != null) {
            if (!executionSet.getAssignmentsinfo().getValue().isEmpty()) {
                dataInputAssociation.addAll(AssignmentParser.parseDataInputAssociation(getId(),
                                                                                       executionSet.getAssignmentsinfo().getValue()));
            }
        }

        return dataInputAssociation;
    }

    @Override
    public List<DataOutputAssociation> getDataOutputAssociation() {
        dataOutputAssociation = IsMultipleInstance.super.getDataOutputAssociation();

        if (!executionSet.getAssignmentsinfo().getValue().isEmpty()) {
            dataOutputAssociation.addAll(AssignmentParser.parseDataOutputAssociation(getId(),
                                                                                     executionSet.getAssignmentsinfo().getValue()));
        }

        return dataOutputAssociation;
    }

    @Override
    public MultiInstanceLoopCharacteristics getMultiInstanceLoopCharacteristics() {
        return IsMultipleInstance.super.getMultiInstanceLoopCharacteristics();
    }

    public void setIoSpecification(IoSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    public void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation) {
        this.dataInputAssociation = dataInputAssociation;
    }

    public void setDataOutputAssociation(List<DataOutputAssociation> dataOutputAssociation) {
        this.dataOutputAssociation = dataOutputAssociation;
    }

    public void setMultiInstanceLoopCharacteristics(MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics) {
        this.multiInstanceLoopCharacteristics = multiInstanceLoopCharacteristics;
    }

    public String getServiceImplementation() {
        return executionSet.getGenericServiceTaskInfo().getValue().getServiceImplementation();
    }

    public void setServiceImplementation(String serviceImplementation) {
        this.serviceImplementation = serviceImplementation;
    }

    public String getServiceInterface() {
        return executionSet.getGenericServiceTaskInfo().getValue().getServiceInterface();
    }

    public void setServiceInterface(String serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public String getServiceOperation() {
        return executionSet.getGenericServiceTaskInfo().getValue().getServiceOperation();
    }

    public void setServiceOperation(String serviceOperation) {
        this.serviceOperation = serviceOperation;
    }

    public String getImplementation() {
        return executionSet.getGenericServiceTaskInfo().getValue().getServiceImplementation();
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getOperationRef() {
        return getId() + "_ServiceOperation";
    }

    public void setOperationRef(String operationRef) {
        this.operationRef = operationRef;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GenericServiceTask) {
            GenericServiceTask other = (GenericServiceTask) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet);
        }
        return false;
    }
}
