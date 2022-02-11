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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNCategories;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNViewDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.HasIncoming;
import org.kie.workbench.common.stunner.bpmn.definition.HasOutgoing;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskType;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.TaskTypes;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Category;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Labels;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.MorphBase;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.MorphProperty;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.MorphPropertyValueBinding;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

@MorphBase(defaultType = NoneTask.class, targets = {BaseNonContainerSubprocess.class})
public abstract class BaseTask extends FlowNode implements BPMNViewDefinition,
                                                           HasIncoming,
                                                           HasOutgoing {

    public static final Set<String> TASK_LABELS = Stream.of("all",
                                                            "lane_child",
                                                            "sequence_start",
                                                            "sequence_end",
                                                            "from_task_event",
                                                            "to_task_event",
                                                            "messageflow_start",
                                                            "messageflow_end",
                                                            "fromtoall",
                                                            "ActivitiesMorph",
                                                            "cm_activity")
            .collect(Collectors.toSet());

    @Category
    @XmlTransient
    public static final transient String category = BPMNCategories.ACTIVITIES;

    @Property
    @XmlTransient
    @MorphProperty(binder = TaskTypeMorphPropertyBinding.class)
    protected TaskType taskType;

    @Property
    @XmlTransient
    protected SimulationSet simulationSet;

    @Property
    @FormField(
            afterElement = "dimensionsSet"
    )
    @Valid
    @XmlTransient
    protected AdvancedData advancedData;

    @XmlTransient
    public static class TaskTypeMorphPropertyBinding implements MorphPropertyValueBinding<TaskType, TaskTypes> {

        private static final Map<TaskTypes, Class<?>> MORPH_TARGETS = Stream.of(
                new AbstractMap.SimpleEntry<>(TaskTypes.NONE, NoneTask.class),
                new AbstractMap.SimpleEntry<>(TaskTypes.USER, UserTask.class),
                new AbstractMap.SimpleEntry<>(TaskTypes.SCRIPT, ScriptTask.class),
                new AbstractMap.SimpleEntry<>(TaskTypes.BUSINESS_RULE, BusinessRuleTask.class),
                new AbstractMap.SimpleEntry<>(TaskTypes.SERVICE_TASK, GenericServiceTask.class))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        @Override
        public TaskTypes getValue(final TaskType property) {
            return property.getValue();
        }

        @Override
        public Map<TaskTypes, Class<?>> getMorphTargets() {
            return MORPH_TARGETS;
        }
    }

    @Labels
    @XmlTransient
    protected final Set<String> labels = new HashSet<>(TASK_LABELS);

    @XmlUnwrappedCollection
    private List<Incoming> incoming = new ArrayList<>();

    @XmlUnwrappedCollection
    private List<Outgoing> outgoing = new ArrayList<>();

    public BaseTask() {
        this("", "", new SimulationSet(), new TaskType(), new AdvancedData());
    }

    public BaseTask(final @MapsTo("name") String name,
                    final @MapsTo("documentation") String documentation,
                    final @MapsTo("simulationSet") SimulationSet simulationSet,
                    final @MapsTo("taskType") TaskType taskType,
                    final @MapsTo("advancedData") AdvancedData advancedData) {
        super(name, documentation, advancedData);
        this.simulationSet = simulationSet;
        this.taskType = taskType;
    }

    public String getCategory() {
        return category;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(final TaskType taskType) {
        this.taskType = taskType;
    }

    public List<Incoming> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<Incoming> incoming) {
        this.incoming = incoming;
    }

    public List<Outgoing> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(List<Outgoing> outgoing) {
        this.outgoing = outgoing;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(getClass()),
                                         super.hashCode(),
                                         Objects.hashCode(taskType),
                                         Objects.hashCode(simulationSet),
                                         Objects.hashCode(labels),
                                         Objects.hashCode(incoming),
                                         Objects.hashCode(outgoing),
                                         Objects.hashCode(advancedData));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseTask) {
            BaseTask other = (BaseTask) o;
            return super.equals(other) &&
                    Objects.equals(taskType, other.taskType) &&
                    Objects.equals(simulationSet, other.simulationSet) &&
                    Objects.equals(labels, other.labels) &&
                    Objects.equals(incoming, other.incoming) &&
                    Objects.equals(outgoing, other.outgoing) &&
                    Objects.equals(advancedData, other.advancedData);
        }
        return false;
    }
}
