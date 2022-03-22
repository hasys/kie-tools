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
import org.kie.workbench.common.stunner.bpmn.definition.BPMNCategories;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.OnEntryScript;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.OnExitScript;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOModel;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.ScriptTypeValue;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskType;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskTypes;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.bpmn.workitem.CustomTaskExecutionSet;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Description;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Category;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Id;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Title;
import org.kie.workbench.common.stunner.core.factory.graph.NodeFactory;
import org.kie.workbench.common.stunner.core.rule.annotation.CanDock;
import org.kie.workbench.common.stunner.core.util.ClassUtils;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition(graphFactory = NodeFactory.class)
@CanDock(roles = {"IntermediateEventOnActivityBoundary"})
@FormDefinition(
        policy = FieldPolicy.ONLY_MARKED,
        startElement = "general",
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "task", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class CustomTask extends BaseCustomTask implements DataIOModel {

    @Property
    @FormField(
            afterElement = "general"
    )
    @Valid
    @XmlTransient
    protected CustomTaskExecutionSet executionSet;

    @Property
    @FormField(
            afterElement = "executionSet"
    )
    @Valid
    @XmlTransient
    protected DataIOSet dataIOSet;

    @Id
    @Title
    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private String taskName;

    @Description
    @XmlTransient
    private String description;

    @Category
    @XmlTransient
    private String category;

    @XmlTransient
    private String defaultHandler;

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

    public static boolean isWorkItem(final Object definition) {
        return ClassUtils.isTypeOf(CustomTask.class,
                                   definition);
    }

    public CustomTask() {
        this("Custom Task",
             "Custom Task",
             BPMNCategories.CUSTOM_TASKS,
             "",
             "Custom Task",
             "",
             new DataIOSet(),
             new CustomTaskExecutionSet(),
             new SimulationSet(),
             new TaskType(TaskTypes.SERVICE_TASK),
             new AdvancedData());
    }

    public CustomTask(@MapsTo("taskName") String taskName,
                      @MapsTo("description") String description,
                      @MapsTo("category") String category,
                      @MapsTo("defaultHandler") String defaultHandler,
                      @MapsTo("name") String name,
                      @MapsTo("documentation") String documentation,
                      @MapsTo("dataIOSet") DataIOSet dataIOSet,
                      @MapsTo("executionSet") CustomTaskExecutionSet executionSet,
                      @MapsTo("simulationSet") SimulationSet simulationSet,
                      @MapsTo("taskType") TaskType taskType,
                      @MapsTo("advancedData") AdvancedData advancedData) {
        super(name,
              documentation,
              simulationSet,
              taskType,
              advancedData);
        this.taskName = taskName;
        this.description = description;
        this.category = category;
        this.defaultHandler = defaultHandler;
        this.dataIOSet = dataIOSet;
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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(String defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public DataIOSet getDataIOSet() {
        return dataIOSet;
    }

    public void setDataIOSet(DataIOSet dataIOSet) {
        this.dataIOSet = dataIOSet;
    }

    public CustomTaskExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(CustomTaskExecutionSet executionSet) {
        this.executionSet = executionSet;
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

        if (!dataIOSet.getAssignmentsinfo().getValue().isEmpty()) {
            String wholeAssignments = dataIOSet.getAssignmentsinfo().getValue();
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

    public IoSpecification getIoSpecification() {
        if (getId() == null) {
            return null;
        }
        ioSpecification = new IoSpecification();

        DataInput taskName = createDataInput("TaskName");
        ioSpecification.getDataInput().add(taskName);
        ioSpecification.getInputSet().getDataInputRefs().add(new DataInputRefs(taskName.getId()));

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

    private DataInput createDataInput(String name) {
        return createDataInput(name, "Object");
    }

    private DataInput createDataInput(String name, String type) {
        DataInput dataInput = new DataInput();
        dataInput.setDtype(type);
        dataInput.setName(name);
        dataInput.setId(getId() + "_" + name + "InputX");

        return dataInput;
    }

    public void setIoSpecification(IoSpecification specification) {
    }

    public List<DataOutputAssociation> getDataOutputAssociation() {
        dataOutputAssociation = new ArrayList<>();

        if (!dataIOSet.getAssignmentsinfo().getValue().isEmpty()) {
            dataOutputAssociation.addAll(AssignmentParser.parseDataOutputAssociation(getId(),
                                                                                     dataIOSet.getAssignmentsinfo().getValue()));
        }

        return dataOutputAssociation;
    }

    public void setDataOutputAssociation(List<DataOutputAssociation> dataOutputAssociation) {
        this.dataOutputAssociation = dataOutputAssociation;
    }

    public List<DataInputAssociation> getDataInputAssociation() {
        dataInputAssociation = new ArrayList<>();

        // Can happen on designer startup during CDI proxies creation phase
        if (getId() != null) {

            DataInputAssociation taskNameAssociation = new DataInputAssociation();
            taskNameAssociation.getTargetRef().setValue(getId() + "_TaskNameInputX");
            taskNameAssociation.getAssignment().setFrom(new From(taskName));
            taskNameAssociation.getAssignment().setTo(new To(getId() + "_TaskNameInputX"));
            dataInputAssociation.add(taskNameAssociation);

            if (!dataIOSet.getAssignmentsinfo().getValue().isEmpty()) {
                dataInputAssociation.addAll(AssignmentParser.parseDataInputAssociation(getId(),
                                                                                       dataIOSet.getAssignmentsinfo().getValue()));
            }
        }

        return dataInputAssociation;
    }

    public void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation) {
        this.dataInputAssociation = dataInputAssociation;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode(),
                                         dataIOSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CustomTask) {
            CustomTask other = (CustomTask) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet) &&
                    dataIOSet.equals(other.dataIOSet);
        }
        return false;
    }
}
