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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNCategories;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNViewDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.BaseSubprocessTaskExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.HasProcessData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.ProcessData;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Category;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Labels;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.MorphBase;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

@MorphBase(defaultType = EmbeddedSubprocess.class)
public abstract class BaseSubprocess extends FlowNode implements BPMNViewDefinition,
                                                                 HasProcessData<ProcessData> {

    @Category
    @XmlTransient
    public static final transient String category = BPMNCategories.SUB_PROCESSES;

    @Property
    @Valid
    @XmlTransient
    protected BackgroundSet backgroundSet;

    @Property
    @XmlTransient
    protected FontSet fontSet;

    @Property
    @XmlTransient
    protected SimulationSet simulationSet;

    @Property
    @XmlTransient
    protected RectangleDimensionsSet dimensionsSet;

    @Labels
    @XmlTransient
    protected final Set<String> labels = new HashSet<>();

    @Property
    @FormField(afterElement = "executionSet")
    @Valid
    @XmlTransient
    private ProcessData processData;

    @XmlElement(name = "property")
    @XmlUnwrappedCollection
    private List<org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Property> properties = new ArrayList<>();

    public BaseSubprocess() {
        this("",
             "",
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(),
             new SimulationSet(),
             new AdvancedData(),
             new ProcessData());
    }

    public BaseSubprocess(final @MapsTo("name") String name,
                          final @MapsTo("documentation") String documentation,
                          final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                          final @MapsTo("fontSet") FontSet fontSet,
                          final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                          final @MapsTo("simulationSet") SimulationSet simulationSet,
                          final @MapsTo("advancedData") AdvancedData advancedData,
                          final @MapsTo("processData") ProcessData processData) {
        super(name, documentation, advancedData);
        this.backgroundSet = backgroundSet;
        this.fontSet = fontSet;
        this.dimensionsSet = dimensionsSet;
        this.simulationSet = simulationSet;
        this.processData = processData;
        this.initLabels();
    }

    protected void initLabels() {
        labels.add("all");
        labels.add("lane_child");
        labels.add("sequence_start");
        labels.add("sequence_end");
        labels.add("messageflow_start");
        labels.add("messageflow_end");
        labels.add("to_task_event");
        labels.add("from_task_event");
        labels.add("fromtoall");
        labels.add("ActivitiesMorph");
        labels.add("cm_stage");
    }

    public String getCategory() {
        return category;
    }

    public BackgroundSet getBackgroundSet() {
        return backgroundSet;
    }

    public void setBackgroundSet(final BackgroundSet backgroundSet) {
        this.backgroundSet = backgroundSet;
    }

    public FontSet getFontSet() {
        return fontSet;
    }

    public void setFontSet(final FontSet fontSet) {
        this.fontSet = fontSet;
    }

    public SimulationSet getSimulationSet() {
        return simulationSet;
    }

    public void setSimulationSet(final SimulationSet simulationSet) {
        this.simulationSet = simulationSet;
    }

    public RectangleDimensionsSet getDimensionsSet() {
        return dimensionsSet;
    }

    public void setDimensionsSet(final RectangleDimensionsSet dimensionsSet) {
        this.dimensionsSet = dimensionsSet;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public abstract BaseSubprocessTaskExecutionSet getExecutionSet();

    public abstract void setExecutionSet(BaseSubprocessTaskExecutionSet executionSet);

    @Override
    public ExtensionElements getExtensionElements() {
        ExtensionElements elements = super.getExtensionElements();

        if (getExecutionSet().getSlaDueDate().getValue() != null && !getExecutionSet().getSlaDueDate().getValue().isEmpty()) {
            MetaData customAutoStart = new MetaData("customSLADueDate",
                                                    getExecutionSet().getSlaDueDate().getValue());
            elements.getMetaData().add(customAutoStart);
        }

        if (getExecutionSet().getIsAsync().getValue()) {
            MetaData customAutoStart = new MetaData("customAsync",
                                                    "true");
            elements.getMetaData().add(customAutoStart);
        }

        return elements.getMetaData().isEmpty() ? null : elements;
    }

    public ProcessData getProcessData() {
        return processData;
    }

    public void setProcessData(final ProcessData processData) {
        this.processData = processData;
    }

    public List<org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Property> getProperties() {
        String value = getProcessData().getProcessVariables();
        if (value == null || value.isEmpty()) {
            return null;
        }

        properties.clear();
        properties.addAll(AssignmentParser.parseProcessVariables(value));

        return properties;
    }

    public List<ItemDefinition> getItemDefinitions() {
        String value = getProcessData().getProcessVariables();
        if (value == null || value.isEmpty()) {
            return Collections.emptyList();
        }

        return AssignmentParser.getAllItemDefinitions(getId(), value);
    }

    public void setProperties(List<org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Property> properties) {
        this.properties = properties;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(getClass()),
                                         super.hashCode(),
                                         Objects.hashCode(backgroundSet),
                                         Objects.hashCode(fontSet),
                                         Objects.hashCode(simulationSet),
                                         Objects.hashCode(dimensionsSet),
                                         Objects.hashCode(advancedData),
                                         Objects.hashCode(properties),
                                         Objects.hashCode(labels));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseSubprocess) {
            BaseSubprocess other = (BaseSubprocess) o;
            return super.equals(other) &&
                    Objects.equals(backgroundSet, other.backgroundSet) &&
                    Objects.equals(fontSet, other.fontSet) &&
                    Objects.equals(simulationSet, other.simulationSet) &&
                    Objects.equals(dimensionsSet, other.dimensionsSet) &&
                    Objects.equals(advancedData, other.advancedData) &&
                    Objects.equals(properties, other.properties) &&
                    Objects.equals(labels, other.labels);
        }
        return false;
    }
}
