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
package org.kie.workbench.common.stunner.bpmn.definition;

import java.util.List;

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Association;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseCatchingIntermediateEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseThrowingIntermediateEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataObjectReference;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EventGateway;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.ExclusiveGateway;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.InclusiveGateway;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Lane;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.ParallelGateway;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.ScriptTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.SequenceFlow;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.TextAnnotation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.UserTask;

public interface BpmnContainer {

    List<StartEvent> getStartEvents();

    List<EndEvent> getEndEvents();

    List<BaseTask> getTasks();

    List<ScriptTask> getScriptTasks();

    List<UserTask> getUserTasks();

    List<SequenceFlow> getSequenceFlows();

    List<BaseSubprocess> getSubProcesses();

    List<Lane> getLanes();

    List<EventGateway> getEventBasedGateways();

    List<InclusiveGateway> getInclusiveGateways();

    List<ExclusiveGateway> getExclusiveGateways();

    List<ParallelGateway> getParallelGateways();

    List<BaseThrowingIntermediateEvent> getIntermediateThrowEvent();

    List<BaseCatchingIntermediateEvent> getIntermediateCatchEvent();

    List<TextAnnotation> getTextAnnotations();

    List<Association> getAssociations();

    List<DataObjectReference> getDataObjectsReference();

    default void clear() {
        getScriptTasks().clear();
        getUserTasks().clear();
        getTasks().clear();
        getStartEvents().clear();
        getEndEvents().clear();
        getSequenceFlows().clear();
        getSubProcesses().clear();
        getLanes().clear();
        getEventBasedGateways().clear();
        getInclusiveGateways().clear();
        getExclusiveGateways().clear();
        getParallelGateways().clear();
        getIntermediateThrowEvent().clear();
        getIntermediateCatchEvent().clear();
        getTextAnnotations().clear();
        getAssociations().clear();
        getDataObjectsReference().clear();
    }

    default void addNode(BPMNViewDefinition node) {
        if (node instanceof ScriptTask) {
            getScriptTasks().add((ScriptTask) node);
        } else if (node instanceof UserTask) {
            getUserTasks().add((UserTask) node);
        } else if (node instanceof BaseTask) {
            getTasks().add((BaseTask) node);
        } else if (node instanceof StartEvent) {
            getStartEvents().add((StartEvent) node);
        } else if (node instanceof EndEvent) {
            getEndEvents().add((EndEvent) node);
        } else if (node instanceof SequenceFlow) {
            List<SequenceFlow> flows = getSequenceFlows();
            if (!flows.contains(node)) {
                flows.add((SequenceFlow) node);
            }
        } else if (node instanceof BaseSubprocess) {
            getSubProcesses().add((BaseSubprocess) node);
        } else if (node instanceof Lane) {
            getLanes().add((Lane) node);
        } else if (node instanceof EventGateway) {
            getEventBasedGateways().add((EventGateway) node);
        } else if (node instanceof InclusiveGateway) {
            getInclusiveGateways().add((InclusiveGateway) node);
        } else if (node instanceof ExclusiveGateway) {
            getExclusiveGateways().add((ExclusiveGateway) node);
        } else if (node instanceof ParallelGateway) {
            getParallelGateways().add((ParallelGateway) node);
        } else if (node instanceof BaseThrowingIntermediateEvent) {
            getIntermediateThrowEvent().add((BaseThrowingIntermediateEvent) node);
        } else if (node instanceof BaseCatchingIntermediateEvent) {
            getIntermediateCatchEvent().add((BaseCatchingIntermediateEvent) node);
        } else if (node instanceof TextAnnotation) {
            getTextAnnotations().add((TextAnnotation) node);
        } else if (node instanceof Association) {
            getAssociations().add((Association) node);
        } else if (node instanceof DataObjectReference) {
            getDataObjectsReference().add((DataObjectReference) node);
        }
    }
}
