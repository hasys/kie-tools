/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.kie.workbench.common.stunner.bpmn.definition.BpmnContainer;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.ProcessData;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

public abstract class BaseContainerSubprocess extends BaseSubprocess implements BpmnContainer {

    @XmlElement(name = "startEvent")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_StartNoneEvent", type = StartNoneEvent.class),
            @XmlElement(name = "_StartCompensationEvent", type = StartCompensationEvent.class),
            @XmlElement(name = "_StartMessageEvent", type = StartMessageEvent.class),
            @XmlElement(name = "_StartSignalEvent", type = StartSignalEvent.class),
            @XmlElement(name = "_StartTimerEvent", type = StartTimerEvent.class),
            @XmlElement(name = "_StartEscalationEvent", type = StartEscalationEvent.class),
            @XmlElement(name = "_StartErrorEvent", type = StartErrorEvent.class),
            @XmlElement(name = "_StartConditionalEvent", type = StartConditionalEvent.class)
    })
    private List<StartEvent> startEvents = new ArrayList<>();

    @XmlElement(name = "laneSet")
    private List<Lane> lanes = new ArrayList<>();

    @XmlElement(name = "textAnnotation")
    private List<TextAnnotation> textAnnotations = new ArrayList<>();

    @XmlElement(name = "association")
    private List<Association> associations = new ArrayList<>();

    @XmlElement(name = "endEvent")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_EndNoneEvent", type = EndNoneEvent.class),
            @XmlElement(name = "_EndTerminateEvent", type = EndTerminateEvent.class),
            @XmlElement(name = "_EndErrorEvent", type = EndErrorEvent.class),
            @XmlElement(name = "_EndEscalationEvent", type = EndEscalationEvent.class),
            @XmlElement(name = "_EndCompensationEvent", type = EndCompensationEvent.class),
            @XmlElement(name = "_EndSignalEvent", type = EndSignalEvent.class),
            @XmlElement(name = "_EndMessageEvent", type = EndMessageEvent.class)
    })
    private List<EndEvent> endEvents = new ArrayList<>();

    @XmlElement(name = "intermediateThrowEvent")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_LinkThrowEvent", type = IntermediateLinkEventThrowing.class),
            @XmlElement(name = "_EscalationThrowEvent", type = IntermediateEscalationEventThrowing.class),
            @XmlElement(name = "_CompensationThrowEvent", type = IntermediateCompensationEventThrowing.class),
            @XmlElement(name = "_SignalThrowEvent", type = IntermediateSignalEventThrowing.class),
            @XmlElement(name = "_MessageThrowEvent", type = IntermediateMessageEventThrowing.class)
    })
    private List<BaseThrowingIntermediateEvent> intermediateThrowEvent = new ArrayList<>();

    @XmlElement(name = "intermediateCatchEvent")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_TimerCatchEvent", type = IntermediateTimerEvent.class),
            @XmlElement(name = "_LinkCatchEvent", type = IntermediateLinkEventCatching.class),
            @XmlElement(name = "_ErrorCatchEvent", type = IntermediateErrorEventCatching.class),
            @XmlElement(name = "_EscalationCatchEvent", type = IntermediateEscalationEvent.class),
            @XmlElement(name = "_CompensationCatchEvent", type = IntermediateCompensationEvent.class),
            @XmlElement(name = "_SignalCatchEvent", type = IntermediateSignalEventCatching.class),
            @XmlElement(name = "_MessageCatchEvent", type = IntermediateMessageEventCatching.class)
    })
    private List<BaseCatchingIntermediateEvent> intermediateCatchEvent = new ArrayList<>();

    @XmlElement(name = "sequenceFlow")
    @XmlUnwrappedCollection
    private List<SequenceFlow> sequenceFlows = new ArrayList<>();

    @XmlElement(name = "task")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_NoneTask", type = NoneTask.class),
            @XmlElement(name = "_CustomTask", type = CustomTask.class)
    })
    private List<BaseTask> tasks = new ArrayList<>();

    @XmlElement(name = "scriptTask")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_ScriptTask", type = ScriptTask.class)
    })
    private List<ScriptTask> scriptTasks = new ArrayList<>();

    @XmlElement(name = "businessRuleTask")
    @XmlUnwrappedCollection
    private List<BusinessRuleTask> businessRuleTask = new ArrayList<>();

    @XmlElement(name = "serviceTask")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_GenericServiceTask", type = GenericServiceTask.class)
    })
    private List<GenericServiceTask> genericServiceTask = new ArrayList<>();

    @XmlElement(name = "userTask")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_UserTask", type = UserTask.class)
    })
    private List<UserTask> userTasks = new ArrayList<>();

    @XmlElement(name = "eventBasedGateway")
    @XmlUnwrappedCollection
    private List<EventGateway> eventBasedGateways = new ArrayList<>();

    @XmlElement(name = "inclusiveGateway")
    @XmlUnwrappedCollection
    private List<InclusiveGateway> inclusiveGateways = new ArrayList<>();

    @XmlElement(name = "exclusiveGateway")
    @XmlUnwrappedCollection
    private List<ExclusiveGateway> exclusiveGateways = new ArrayList<>();

    @XmlElement(name = "parallelGateway")
    @XmlUnwrappedCollection
    private List<ParallelGateway> parallelGateways = new ArrayList<>();

    @XmlElement(name = "dataObjectReference")
    @XmlUnwrappedCollection
    private List<DataObjectReference> dataObjectsReference = new ArrayList<>();

    @XmlElement(name = "subProcess")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_EventSubProcess", type = EventSubprocess.class),
            @XmlElement(name = "_EmbeddedSubProcess", type = EmbeddedSubprocess.class),
            @XmlElement(name = "_MISubProcess", type = MultipleInstanceSubprocess.class)
    })
    private List<BaseSubprocess> subProcesses = new ArrayList<>();

    @XmlElement(name = "adHocSubProcess")
    @XmlUnwrappedCollection
    private List<AdHocSubprocess> adHocSubProcess = new ArrayList<>();

    @XmlElement(name = "callActivity")
    @XmlUnwrappedCollection
    private List<ReusableSubprocess> callActivities = new ArrayList<>();

    public BaseContainerSubprocess() {
        this("",
             "",
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(),
             new SimulationSet(),
             new AdvancedData(),
             new ProcessData());
    }

    public BaseContainerSubprocess(final @MapsTo("name") String name,
                                   final @MapsTo("documentation") String documentation,
                                   final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                                   final @MapsTo("fontSet") FontSet fontSet,
                                   final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                                   final @MapsTo("simulationSet") SimulationSet simulationSet,
                                   final @MapsTo("advancedData") AdvancedData advancedData,
                                   final @MapsTo("processData") ProcessData processData) {
        super(name, documentation, backgroundSet, fontSet, dimensionsSet, simulationSet, advancedData, processData);
    }

    @Override
    public List<StartEvent> getStartEvents() {
        return startEvents;
    }

    public void setStartEvents(List<StartEvent> startEvents) {
        this.startEvents = startEvents;
    }

    public List<EndEvent> getEndEvents() {
        return endEvents;
    }

    public void setEndEvents(List<EndEvent> endEvents) {
        this.endEvents = endEvents;
    }

    public List<BaseTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<BaseTask> tasks) {
        this.tasks = tasks;
    }

    public List<ScriptTask> getScriptTasks() {
        return scriptTasks;
    }

    public void setScriptTasks(List<ScriptTask> scriptTasks) {
        this.scriptTasks = scriptTasks;
    }

    public List<UserTask> getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(List<UserTask> userTasks) {
        this.userTasks = userTasks;
    }

    @Override
    public List<GenericServiceTask> getGenericServiceTask() {
        return genericServiceTask;
    }

    public void setGenericServiceTask(List<GenericServiceTask> genericServiceTask) {
        this.genericServiceTask = genericServiceTask;
    }

    public List<SequenceFlow> getSequenceFlows() {
        return sequenceFlows;
    }

    public void setSequenceFlows(List<SequenceFlow> sequenceFlows) {
        this.sequenceFlows = sequenceFlows;
    }

    public List<BaseSubprocess> getSubProcesses() {
        return subProcesses;
    }

    public void setSubProcesses(List<BaseSubprocess> subProcesses) {
        this.subProcesses = subProcesses;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
        this.lanes = lanes;
    }

    public List<EventGateway> getEventBasedGateways() {
        return eventBasedGateways;
    }

    public void setEventBasedGateways(List<EventGateway> eventBasedGateways) {
        this.eventBasedGateways = eventBasedGateways;
    }

    public List<InclusiveGateway> getInclusiveGateways() {
        return inclusiveGateways;
    }

    public void setInclusiveGateways(List<InclusiveGateway> inclusiveGateways) {
        this.inclusiveGateways = inclusiveGateways;
    }

    public List<ExclusiveGateway> getExclusiveGateways() {
        return exclusiveGateways;
    }

    public void setExclusiveGateways(List<ExclusiveGateway> exclusiveGateways) {
        this.exclusiveGateways = exclusiveGateways;
    }

    public List<ParallelGateway> getParallelGateways() {
        return parallelGateways;
    }

    public void setParallelGateways(List<ParallelGateway> parallelGateways) {
        this.parallelGateways = parallelGateways;
    }

    public List<BaseThrowingIntermediateEvent> getIntermediateThrowEvent() {
        return intermediateThrowEvent;
    }

    public void setIntermediateThrowEvent(List<BaseThrowingIntermediateEvent> intermediateThrowEvent) {
        this.intermediateThrowEvent = intermediateThrowEvent;
    }

    public List<BaseCatchingIntermediateEvent> getIntermediateCatchEvent() {
        return intermediateCatchEvent;
    }

    public void setIntermediateCatchEvent(List<BaseCatchingIntermediateEvent> intermediateCatchEvent) {
        this.intermediateCatchEvent = intermediateCatchEvent;
    }

    public List<TextAnnotation> getTextAnnotations() {
        return textAnnotations;
    }

    public void setTextAnnotations(List<TextAnnotation> textAnnotations) {
        this.textAnnotations = textAnnotations;
    }

    public List<Association> getAssociations() {
        return associations;
    }

    public void setAssociations(List<Association> associations) {
        this.associations = associations;
    }

    public List<DataObjectReference> getDataObjectsReference() {
        return dataObjectsReference;
    }

    public void setDataObjectsReference(List<DataObjectReference> dataObjectsReference) {
        this.dataObjectsReference = dataObjectsReference;
    }

    @Override
    public List<ReusableSubprocess> getCallActivities() {
        return callActivities;
    }

    public void setCallActivities(List<ReusableSubprocess> callActivities) {
        this.callActivities = callActivities;
    }

    public List<AdHocSubprocess> getAdHocSubProcess() {
        return adHocSubProcess;
    }

    public void setAdHocSubProcess(List<AdHocSubprocess> adHocSubProcess) {
        this.adHocSubProcess = adHocSubProcess;
    }

    public List<BusinessRuleTask> getBusinessRuleTask() {
        return businessRuleTask;
    }

    public void setBusinessRuleTask(List<BusinessRuleTask> businessRuleTask) {
        this.businessRuleTask = businessRuleTask;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(startEvents),
                                         Objects.hashCode(endEvents),
                                         Objects.hashCode(intermediateThrowEvent),
                                         Objects.hashCode(intermediateCatchEvent),
                                         Objects.hashCode(tasks),
                                         Objects.hashCode(scriptTasks),
                                         Objects.hashCode(sequenceFlows),
                                         Objects.hashCode(lanes),
                                         Objects.hashCode(subProcesses),
                                         Objects.hashCode(textAnnotations),
                                         Objects.hashCode(associations),
                                         Objects.hashCode(adHocSubProcess),
                                         Objects.hashCode(businessRuleTask),
                                         Objects.hashCode(callActivities));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseContainerSubprocess) {
            BaseContainerSubprocess other = (BaseContainerSubprocess) o;
            return Objects.equals(startEvents, other.startEvents)
                    && Objects.equals(endEvents, other.endEvents)
                    && Objects.equals(intermediateCatchEvent, other.intermediateCatchEvent)
                    && Objects.equals(intermediateThrowEvent, other.intermediateThrowEvent)
                    && Objects.equals(tasks, other.tasks)
                    && Objects.equals(scriptTasks, other.scriptTasks)
                    && Objects.equals(sequenceFlows, other.sequenceFlows)
                    && Objects.equals(lanes, other.lanes)
                    && Objects.equals(subProcesses, other.subProcesses)
                    && Objects.equals(textAnnotations, other.textAnnotations)
                    && Objects.equals(associations, other.associations)
                    && Objects.equals(adHocSubProcess, other.adHocSubProcess)
                    && Objects.equals(businessRuleTask, other.businessRuleTask)
                    && Objects.equals(callActivities, other.callActivities);
        }
        return false;
    }
}
