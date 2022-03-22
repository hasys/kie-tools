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
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.OnEntryScript;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.OnExitScript;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOModel;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.BusinessRuleTaskExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.RuleLanguage;
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

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@CanDock(roles = {"IntermediateEventOnActivityBoundary"})
@Morph(base = BaseTask.class)
@FormDefinition(
        startElement = "general",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "businessRuleTask", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class BusinessRuleTask extends BaseTask implements DataIOModel {

    @XmlTransient
    public static final String EXECUTION_SET = "executionSet";

    @Property
    @FormField(
            afterElement = "general"
    )
    @Valid
    @XmlTransient
    protected BusinessRuleTaskExecutionSet executionSet;

    @Property
    @FormField(
            afterElement = "executionSet"
    )
    @Valid
    @XmlTransient
    protected DataIOSet dataIOSet;

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

    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private String ruleFlowGroup;

    @XmlAttribute
    private String implementation;

    public BusinessRuleTask() {
        this("Task",
             "",
             new BusinessRuleTaskExecutionSet(),
             new DataIOSet(),
             new SimulationSet(),
             new TaskType(TaskTypes.BUSINESS_RULE),
             new AdvancedData());
    }

    public BusinessRuleTask(final @MapsTo("name") String name,
                            final @MapsTo("documentation") String documentation,
                            final @MapsTo("executionSet") BusinessRuleTaskExecutionSet executionSet,
                            final @MapsTo("dataIOSet") DataIOSet dataIOSet,
                            final @MapsTo("simulationSet") SimulationSet simulationSet,
                            final @MapsTo("taskType") TaskType taskType,
                            final @MapsTo("advancedData") AdvancedData advancedData) {
        super(name,
              documentation,
              simulationSet,
              taskType,
              advancedData);
        this.executionSet = executionSet;
        this.dataIOSet = dataIOSet;
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
        itemDefinitions.add(new ItemDefinition("_" + getId() + "_namespaceInputXItem", "java.lang.String"));
        itemDefinitions.add(new ItemDefinition("_" + getId() + "_modelInputXItem", "java.lang.String"));
        itemDefinitions.add(new ItemDefinition("_" + getId() + "_decisionInputXItem", "java.lang.String"));

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

        if (RuleLanguage.DMN.equals(executionSet.getRuleLanguage().getValue())) {
            DataInput namespace = createDataInput("namespace");
            ioSpecification.getDataInput().add(namespace);
            ioSpecification.getInputSet().getDataInputRefs().add(new DataInputRefs(namespace.getId()));

            DataInput model = createDataInput("model");
            ioSpecification.getDataInput().add(model);
            ioSpecification.getInputSet().getDataInputRefs().add(new DataInputRefs(model.getId()));

            DataInput decision = createDataInput("decision");
            ioSpecification.getDataInput().add(decision);
            ioSpecification.getInputSet().getDataInputRefs().add(new DataInputRefs(decision.getId()));
        }

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
        return createDataInput(name, "java.lang.String");
    }

    private DataInput createDataInput(String name, String type) {
        DataInput dataInput = new DataInput();
        dataInput.setDtype(type);
        dataInput.setName(name);
        dataInput.setId(getId() + "_" + name + "InputX");
        dataInput.setItemSubjectRef("_" + dataInput.getId() + "Item");

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

            if (RuleLanguage.DMN.equals(executionSet.getRuleLanguage().getValue())) {
                DataInputAssociation namespaceAssociation = new DataInputAssociation();
                namespaceAssociation.getTargetRef().setValue(getId() + "_namespaceInputX");
                namespaceAssociation.getAssignment().setFrom(new From(executionSet.getNamespace().getValue()));
                namespaceAssociation.getAssignment().setTo(new To(getId() + "_namespaceInputX"));
                dataInputAssociation.add(namespaceAssociation);

                DataInputAssociation decisionAssociation = new DataInputAssociation();
                decisionAssociation.getTargetRef().setValue(getId() + "_decisionInputX");
                decisionAssociation.getAssignment().setFrom(new From(executionSet.getDecisionName().getValue()));
                decisionAssociation.getAssignment().setTo(new To(getId() + "_decisionInputX"));
                dataInputAssociation.add(decisionAssociation);

                DataInputAssociation model = new DataInputAssociation();
                model.getTargetRef().setValue(getId() + "_modelInputX");
                model.getAssignment().setFrom(new From(executionSet.getDmnModelName().getValue()));
                model.getAssignment().setTo(new To(getId() + "_modelInputX"));
                dataInputAssociation.add(model);
            }

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

    public BusinessRuleTaskExecutionSet getExecutionSet() {
        return executionSet;
    }

    public DataIOSet getDataIOSet() {
        return dataIOSet;
    }

    public void setExecutionSet(final BusinessRuleTaskExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    public void setDataIOSet(final DataIOSet dataIOSet) {
        this.dataIOSet = dataIOSet;
    }

    public String getRuleFlowGroup() {
        if (RuleLanguage.DMN.equals(executionSet.getRuleLanguage().getValue())) {
            return null;
        }
        return executionSet.getRuleFlowGroup().getName();
    }

    public void setRuleFlowGroup(String ruleFlowGroup) {
        this.ruleFlowGroup = ruleFlowGroup;
    }

    public String getImplementation() {
        return executionSet.getRuleLanguage().getValue();
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode(),
                                         dataIOSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BusinessRuleTask) {
            BusinessRuleTask other = (BusinessRuleTask) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet) &&
                    dataIOSet.equals(other.dataIOSet);
        }
        return false;
    }
}
