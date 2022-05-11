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

package org.kie.workbench.common.stunner.bpmn.client.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagram;
import org.kie.workbench.common.stunner.bpmn.definition.FlowElement;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseNonContainerSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseUserTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BusinessRuleTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.CustomTask;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndErrorEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndMessageEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EndSignalEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateErrorEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateEscalationEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateMessageEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateMessageEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateSignalEventCatching;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IntermediateSignalEventThrowing;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.MultipleInstanceSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Process;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartErrorEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartEscalationEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartMessageEvent;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.StartSignalEvent;
import org.kie.workbench.common.stunner.bpmn.definition.property.cm.CaseFileVariables;
import org.kie.workbench.common.stunner.bpmn.definition.property.cm.CaseManagementSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.AssignmentsInfo;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.MultipleInstanceSubprocessTaskExecutionSet;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.graph.Edge;
import org.kie.workbench.common.stunner.core.graph.Element;
import org.kie.workbench.common.stunner.core.graph.Graph;
import org.kie.workbench.common.stunner.core.graph.Node;
import org.kie.workbench.common.stunner.core.graph.content.view.View;
import org.kie.workbench.common.stunner.core.graph.util.GraphUtils;
import org.kie.workbench.common.stunner.core.util.StringUtils;
import org.uberfire.commons.Pair;

import static org.kie.workbench.common.stunner.bpmn.client.util.VariableUtils.FindVariableUsagesFlag.CASE_FILE_VARIABLE;
import static org.kie.workbench.common.stunner.core.util.StringUtils.isEmpty;

public class VariableUtils {

    public enum FindVariableUsagesFlag {
        CASE_FILE_VARIABLE
    }

    private static final String PROPERTY_IN_PREFIX = "[din]";
    private static final String PROPERTY_OUT_PREFIX = "[dout]";
    private final static BiFunction<String, Pair<FlowElement, Node<View<FlowElement>, Edge>>, Collection<VariableUsage>> NO_USAGES = (s, pair) -> Collections.emptyList();
    private final static Map<Predicate<FlowElement>, BiFunction<String, Pair<FlowElement, Node<View<FlowElement>, Edge>>, Collection<VariableUsage>>> findFunctions = buildFindFunctions();

    @SuppressWarnings("unchecked")
    public static Collection<VariableUsage> findVariableUsages(Graph graph, String variableName,
                                                               Set<FindVariableUsagesFlag> flags) {
        if (flags.contains(CASE_FILE_VARIABLE)) {
            variableName = CaseFileVariables.CASE_FILE_PREFIX + variableName;
        }

        return findVariableUsages(graph.nodes(), variableName);
    }

    public static Collection<VariableUsage> findVariableUsages(Node node, String variableName) {
        return findVariableUsages(Collections.singletonList(node), variableName);
    }

    public static boolean matchesProcessID(Graph graph, String variableName) {
        if (StringUtils.isEmpty(variableName)) {
            return false;
        }
        Iterable<Node> nodes = graph.nodes();
        return StreamSupport.stream(nodes.spliterator(), false)
                .filter(VariableUtils::isBPMNDiagramImpl)
                .map(node -> ((View) node.getContent()))
                .map(view -> (Process) view.getDefinition())
                .anyMatch(bpmnDiagram -> Objects.equals(bpmnDiagram.getId(), variableName));
    }

    @SuppressWarnings("unchecked")
    private static Collection<VariableUsage> findVariableUsages(Iterable<Node> nodes, String variableName) {
        if (StringUtils.isEmpty(variableName)) {
            return Collections.EMPTY_LIST;
        }
        return StreamSupport.stream(nodes.spliterator(), false)
                .filter(VariableUtils::isBPMNDefinition)
                .map(node -> (Node<View<FlowElement>, Edge>) node)
                .map(node -> lookupFindFunction(node.getContent().getDefinition()).orElse(NO_USAGES).apply(variableName, Pair.newPair(node.getContent().getDefinition(), node)))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private static Collection<VariableUsage> findVariableUsages(String variableName, AssignmentsInfo assignmentsInfo, String displayName, Node<View<FlowElement>, Edge> node) {
        Collection<VariableUsage> result = new ArrayList<>();
        if (assignmentsInfo != null) {
            Map<String, VariableUsage> decodedVariableUsages = decodeVariableUsages(assignmentsInfo.getValue(), node, displayName);
            if (decodedVariableUsages.containsKey(variableName)) {
                result.add(decodedVariableUsages.get(variableName));
            }
        }
        return result;
    }

    private static Collection<VariableUsage> findVariableUsages(String variableName, MultipleInstanceSubprocess subprocess, Node<View<FlowElement>, Edge> node) {
        final Collection<VariableUsage> result = new ArrayList<>();
        addVariableUsages(result, variableName,
                          subprocess.getExecutionSet().getMultipleInstanceCollectionInput().getValue(), subprocess.getExecutionSet().getMultipleInstanceDataInput().getValue(),
                          subprocess.getExecutionSet().getMultipleInstanceCollectionOutput().getValue(), subprocess.getExecutionSet().getMultipleInstanceDataOutput().getValue(),
                          subprocess.getName(), node);
        return result;
    }

    private static Collection<VariableUsage> findVariableUsages(String variableName, BaseUserTask userTask, Node<View<FlowElement>, Edge> node) {
        final String displayName = userTask.getName();
        final Collection<VariableUsage> result = findVariableUsages(variableName, userTask.getExecutionSet().getAssignmentsinfo(), displayName, node);
        addVariableUsages(result, variableName,
                          userTask.getExecutionSet().getMultipleInstanceCollectionInput().getValue(), userTask.getExecutionSet().getMultipleInstanceDataInput().getValue(),
                          userTask.getExecutionSet().getMultipleInstanceCollectionOutput().getValue(), userTask.getExecutionSet().getMultipleInstanceDataOutput().getValue(),
                          displayName, node);
        return result;
    }

    private static Collection<VariableUsage> findVariableUsages(String variableName, BaseNonContainerSubprocess subprocess, Node<View<FlowElement>, Edge> node) {
        final String displayName = subprocess.getName();
        final Collection<VariableUsage> result = findVariableUsages(variableName, subprocess.getDataIOSet().getAssignmentsinfo(), displayName, node);
        MultipleInstanceSubprocessTaskExecutionSet executionSet = (MultipleInstanceSubprocessTaskExecutionSet) subprocess.getExecutionSet();
        addVariableUsages(result, variableName,
                          executionSet.getMultipleInstanceCollectionInput().getValue(), executionSet.getMultipleInstanceDataInput().getValue(),
                          executionSet.getMultipleInstanceCollectionOutput().getValue(), executionSet.getMultipleInstanceDataOutput().getValue(),
                          displayName, node);
        return result;
    }

    private static void addVariableUsages(Collection<VariableUsage> variableUsages, String variableName,
                                          String miInputCollection, String miDataInput,
                                          String miOutputCollection, String miDataOutput,
                                          String displayName, Node<View<FlowElement>, Edge> node) {
        if (variableName.equals(miInputCollection)) {
            variableUsages.add(new VariableUsage(variableName, VariableUsage.USAGE_TYPE.MULTIPLE_INSTANCE_INPUT_COLLECTION, node, displayName));
        }
        if (variableName.equals(miDataInput)) {
            variableUsages.add(new VariableUsage(variableName, VariableUsage.USAGE_TYPE.MULTIPLE_INSTANCE_DATA_INPUT, node, displayName));
        }
        if (variableName.equals(miOutputCollection)) {
            variableUsages.add(new VariableUsage(variableName, VariableUsage.USAGE_TYPE.MULTIPLE_INSTANCE_OUTPUT_COLLECTION, node, displayName));
        }
        if (variableName.equals(miDataOutput)) {
            variableUsages.add(new VariableUsage(variableName, VariableUsage.USAGE_TYPE.MULTIPLE_INSTANCE_DATA_OUTPUT, node, displayName));
        }
    }

    private static boolean isBPMNDefinition(Node node) {
        return node.getContent() instanceof View &&
                ((View) node.getContent()).getDefinition() instanceof FlowElement;
    }

    protected static boolean isBPMNDiagramImpl(Node node) {
        return node.getContent() instanceof View &&
                ((View) node.getContent()).getDefinition() instanceof Process;
    }

    private static String getDisplayName(FlowElement definition) {
        return definition.getName();
    }

    private static Map<String, VariableUsage> decodeVariableUsages(String encodedAssignments, Node node, String displayName) {
        Map<String, VariableUsage> variableUsages = new HashMap<>();
        if (isEmpty(encodedAssignments)) {
            return variableUsages;
        }
        String[] encodedParts = encodedAssignments.split("\\|");
        if (encodedParts.length != 5) {
            return variableUsages;
        }
        String encodedVariablesList = encodedParts[4];
        if (!isEmpty(encodedVariablesList)) {
            String[] variablesList = encodedVariablesList.split(",");
            Arrays.stream(variablesList)
                    .filter(variableDef -> !isEmpty(variableDef))
                    .forEach(variableDef -> {
                        String variableName = null;
                        VariableUsage.USAGE_TYPE usageType = null;
                        String unPrefixedVariableDef;
                        String[] variableDefParts;
                        if (variableDef.startsWith(PROPERTY_IN_PREFIX)) {
                            unPrefixedVariableDef = variableDef.substring(PROPERTY_IN_PREFIX.length());
                            if (!isEmpty(unPrefixedVariableDef)) {
                                variableDefParts = unPrefixedVariableDef.split("->");
                                variableName = variableDefParts[0];
                                usageType = VariableUsage.USAGE_TYPE.INPUT_VARIABLE;
                            }
                        } else if (variableDef.startsWith(PROPERTY_OUT_PREFIX)) {
                            unPrefixedVariableDef = variableDef.substring(PROPERTY_OUT_PREFIX.length());
                            if (!isEmpty(unPrefixedVariableDef)) {
                                variableDefParts = unPrefixedVariableDef.split("->");
                                variableName = variableDefParts[1];
                                usageType = VariableUsage.USAGE_TYPE.OUTPUT_VARIABLE;
                            }
                        }
                        if (!isEmpty(variableName)) {
                            VariableUsage variableUsage = variableUsages.get(variableName);
                            if (variableUsage == null) {
                                variableUsage = new VariableUsage(variableName, usageType, node, displayName);
                                variableUsages.put(variableUsage.getVariableName(), variableUsage);
                            }
                            if (variableUsage.getUsageType() != usageType) {
                                variableUsage.setUsageType(VariableUsage.USAGE_TYPE.INPUT_OUTPUT_VARIABLE);
                            }
                        }
                    });
        }
        return variableUsages;
    }

    public static String encodeProcessVariables(Diagram diagram, Node selectedElement) {
        Element parent = null;
        if (selectedElement != null) {
            parent = GraphUtils.getParent(selectedElement);
        }
        Iterator<Element> it = diagram.getGraph().nodes().iterator();
        StringBuffer variables = new StringBuffer();
        while (it.hasNext()) {
            Element element = it.next();
            if (element.getContent() instanceof View) {
                Object oDefinition = ((View) element.getContent()).getDefinition();
                if ((oDefinition instanceof BPMNDiagram)) {
                    BPMNDiagram bpmnDiagram = (BPMNDiagram) oDefinition;
                    String processVariables = bpmnDiagram.getProcessData().getProcessVariables();
                    if (processVariables != null && !processVariables.isEmpty()) {
                        if (variables.length() > 0) {
                            variables.append(",");
                        }
                        variables.append(processVariables);
                    }
                    CaseManagementSet caseManagementSet = bpmnDiagram.getCaseManagementSet();
                    if (caseManagementSet != null) {
                        CaseFileVariables caseFileVariables = caseManagementSet.getCaseFileVariables();
                        if (caseFileVariables != null) {
                            if (variables.length() > 0) {
                                variables.append(",");
                            }
                            variables.append(caseFileVariables.getRawValue());
                        }
                    }
                }
                if ((Objects.nonNull(parent) && Objects.equals(parent, element)) || Objects.isNull(selectedElement)) {
                    String subprocessVariables = null;
                    if (oDefinition instanceof BaseSubprocess) {
                        BaseSubprocess subprocess = (BaseSubprocess) oDefinition;
                        subprocessVariables = subprocess.getProcessData().getProcessVariables();
                    }
                    if (subprocessVariables != null && !subprocessVariables.isEmpty()) {
                        if (variables.length() > 0) {
                            variables.append(",");
                        }
                        variables.append(subprocessVariables);
                    }
                }
            }
        }
        return variables.toString();
    }

    private static Optional<BiFunction<String, Pair<FlowElement, Node<View<FlowElement>, Edge>>, Collection<VariableUsage>>> lookupFindFunction(FlowElement definition) {
        //This code should ideally be based on an iteration plus the invocation of Class.isAssignableFrom method, but unfortunately not available in GWT client classes
        return findFunctions.entrySet().stream()
                .filter(entry -> entry.getKey().test(definition))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    private static Map<Predicate<FlowElement>, BiFunction<String, Pair<FlowElement, Node<View<FlowElement>, Edge>>, Collection<VariableUsage>>> buildFindFunctions() {
        Map<Predicate<FlowElement>, BiFunction<String, Pair<FlowElement, Node<View<FlowElement>, Edge>>, Collection<VariableUsage>>> findFunctions = new HashMap<>();
        findFunctions.put(d -> d instanceof BusinessRuleTask, (s, pair) -> findVariableUsages(s, ((BusinessRuleTask) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof BaseUserTask, (s, pair) -> findVariableUsages(s, ((BaseUserTask) pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof CustomTask, (s, pair) -> findVariableUsages(s, ((CustomTask) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof EndErrorEvent, (s, pair) -> findVariableUsages(s, ((EndErrorEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof EndEscalationEvent, (s, pair) -> findVariableUsages(s, ((EndEscalationEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof EndMessageEvent, (s, pair) -> findVariableUsages(s, ((EndMessageEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof EndSignalEvent, (s, pair) -> findVariableUsages(s, ((EndSignalEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof IntermediateErrorEventCatching, (s, pair) -> findVariableUsages(s, ((IntermediateErrorEventCatching) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof IntermediateMessageEventCatching, (s, pair) -> findVariableUsages(s, ((IntermediateMessageEventCatching) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof IntermediateSignalEventCatching, (s, pair) -> findVariableUsages(s, ((IntermediateSignalEventCatching) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof IntermediateEscalationEvent, (s, pair) -> findVariableUsages(s, ((IntermediateEscalationEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof IntermediateEscalationEventThrowing, (s, pair) -> findVariableUsages(s, ((IntermediateEscalationEventThrowing) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof IntermediateMessageEventThrowing, (s, pair) -> findVariableUsages(s, ((IntermediateMessageEventThrowing) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof IntermediateSignalEventThrowing, (s, pair) -> findVariableUsages(s, ((IntermediateSignalEventThrowing) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof StartErrorEvent, (s, pair) -> findVariableUsages(s, ((StartErrorEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof StartEscalationEvent, (s, pair) -> findVariableUsages(s, ((StartEscalationEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof StartMessageEvent, (s, pair) -> findVariableUsages(s, ((StartMessageEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof StartSignalEvent, (s, pair) -> findVariableUsages(s, ((StartSignalEvent) pair.getK1()).getDataIOSet().getAssignmentsinfo(), getDisplayName(pair.getK1()), pair.getK2()));
        findFunctions.put(d -> d instanceof BaseNonContainerSubprocess, (s, pair) -> findVariableUsages(s, (BaseNonContainerSubprocess) pair.getK1(), pair.getK2()));
        findFunctions.put(d -> d instanceof MultipleInstanceSubprocess, (s, pair) -> findVariableUsages(s, (MultipleInstanceSubprocess) pair.getK1(), pair.getK2()));
        return findFunctions;
    }
}