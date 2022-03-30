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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlCData;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.NotEmpty;
import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FieldParam;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.forms.adf.definitions.annotations.field.selector.SelectorDataProvider;
import org.kie.workbench.common.forms.adf.definitions.annotations.metaModel.FieldValue;
import org.kie.workbench.common.forms.adf.definitions.settings.FieldPolicy;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.selectors.listBox.type.ListBoxFieldType;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.textArea.type.TextAreaFieldType;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNCategories;
import org.kie.workbench.common.stunner.bpmn.definition.BPMNDiagram;
import org.kie.workbench.common.stunner.bpmn.definition.BpmnContainer;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.Global;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.Import;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.cm.CaseManagementSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.DiagramSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.GlobalVariables;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.imports.DefaultImport;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.imports.Imports;
import org.kie.workbench.common.stunner.bpmn.definition.property.diagram.imports.ImportsValue;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.ProcessData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.RootProcessAdvancedData;
import org.kie.workbench.common.stunner.bpmn.forms.model.ImportsFieldType;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Category;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Labels;
import org.kie.workbench.common.stunner.core.definition.annotation.property.Value;
import org.kie.workbench.common.stunner.core.rule.annotation.CanContain;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.kie.workbench.common.stunner.core.util.StringUtils;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition()
@CanContain(roles = {"all"})
@FormDefinition(
        startElement = "caseManagementSet",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "process", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class Process implements BPMNDiagram<DiagramSet, ProcessData, RootProcessAdvancedData>,
                                BpmnContainer {

    @Category
    public static final transient String category = BPMNCategories.CONTAINERS;
    public static final String ADHOC = "adHoc";
    public static final String PROCESS_DATA = "processData";
    public static final String ADVANCED_DATA = "advancedData";
    public static final String CASE_MANAGEMENT_SET = "caseManagementSet";

    @Valid
    @Value
    @FieldValue
    @NotNull
    @NotEmpty
    @Property
    @XmlAttribute
    @FormField(afterElement = "caseManagementSet")
    private String name;

    @Property
    @Valid
    @Value
    @FieldValue
    @FormField(
            type = TextAreaFieldType.class,
            afterElement = "name"
    )
    @XmlCData
    private String documentation;

    @Property
    @Valid
    @Value
    @NotNull
    @NotEmpty
    @FieldValue
    @XmlAttribute
    @FormField(afterElement = "documentation")
    private String id;

    @Property
    @Valid
    @Value
    @FieldValue
    @NotNull
    @NotEmpty
    @FormField(afterElement = "id")
    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private String packageName;

    @Property
    @Valid
    @Value
    @NotNull
    @NotEmpty
    @FieldValue
    @XmlAttribute
    @FormField(
            type = ListBoxFieldType.class,
            settings = {@FieldParam(name = "addEmptyOption", value = "false")},
            afterElement = "packageName"
    )
    @SelectorDataProvider(
            type = SelectorDataProvider.ProviderType.CLIENT,
            className = "org.kie.workbench.common.stunner.bpmn.client.dataproviders.ProcessTypeProvider"
    )
    private String processType;

    @Property
    @Valid
    @Value
    @FieldValue
    @NotNull
    @NotEmpty
    @FormField(afterElement = "processType")
    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private String version;

    @Property
    @Valid
    @Value
    @FieldValue
    @FormField(afterElement = "version")
    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private Boolean adHoc;

    @Property
    @Valid
    @Value
    @FieldValue
    @FormField(afterElement = ADHOC)
    @XmlTransient
    private String processInstanceDescription;

    @Property
    @Valid
    @XmlTransient
    @FormField(
            afterElement = "processInstanceDescription",
            type = ImportsFieldType.class
    )
    private Imports imports;

    @Property
    @Value
    @FieldValue
    @FormField(afterElement = "imports")
    @XmlAttribute(name = "isExecutable")
    private Boolean executable;

    @Property
    @Value
    @FieldValue
    @FormField(afterElement = "executable")
    @XmlTransient
    private String slaDueDate;

    @Property
    @Valid
    @XmlTransient
    @FormField(afterElement = "slaDueDate")
    protected ProcessData processData;

    @Property
    @Valid
    @XmlTransient
    @FormField(afterElement = PROCESS_DATA)
    protected RootProcessAdvancedData advancedData;

    @Property
    @FormField
    @XmlTransient
    protected CaseManagementSet caseManagementSet;

    @Property
    @XmlTransient
    private BackgroundSet backgroundSet;

    @Property
    @XmlTransient
    private FontSet fontSet;

    @Property
    @XmlTransient
    protected RectangleDimensionsSet dimensionsSet;

    @XmlElement(name = "adHocSubProcess")
    @XmlUnwrappedCollection
    private List<AdHocSubprocess> adHocSubProcess = new ArrayList<>();

    @Labels
    private final Set<String> labels = new HashSet<>(Arrays.asList("canContainArtifacts",
                                                                   "diagram"));

    public static final Double WIDTH = 950d;
    public static final Double HEIGHT = 950d;

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

    @XmlElement(name = "userTask")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_UserTask", type = UserTask.class)
    })
    private List<UserTask> userTasks = new ArrayList<>();

    @XmlElement(name = "serviceTask")
    @XmlUnwrappedCollection
    @XmlElements({
            @XmlElement(name = "_GenericServiceTask", type = GenericServiceTask.class)
    })
    private List<GenericServiceTask> genericServiceTask = new ArrayList<>();

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

    @XmlElement(name = "dataObject")
    @XmlUnwrappedCollection
    private List<DataObject> dataObjects = new ArrayList<>();

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

    @XmlElement(name = "businessRuleTask")
    @XmlUnwrappedCollection
    private List<BusinessRuleTask> businessRuleTask = new ArrayList<>();

    @XmlElement(name = "property")
    @XmlUnwrappedCollection
    private List<org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Property> properties = new ArrayList<>();

    @XmlElement(name = "callActivity")
    @XmlUnwrappedCollection
    private List<ReusableSubprocess> callActivities = new ArrayList<>();

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
    This variable will be always null and getter/setter will return data from other Execution sets.
    Execution sets not removed due to how forms works now, should be refactored during the migration
    to the new forms.
     */
    @XmlElement
    private ExtensionElements extensionElements;

    public Process() {
        this("",
             "",
             "",
             "",
             "",
             "",
             false,
             "",
             new Imports(),
             false,
             "",
             new ProcessData(),
             new CaseManagementSet(),
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(WIDTH,
                                        HEIGHT),
             new RootProcessAdvancedData());
    }

    public Process(final @MapsTo("name") String name,
                   final @MapsTo("documentation") String documentation,
                   final @MapsTo("id") String id,
                   final @MapsTo("packageName") String packageName,
                   final @MapsTo("processType") String processType,
                   final @MapsTo("version") String version,
                   final @MapsTo(ADHOC) boolean adHoc,
                   final @MapsTo("processInstanceDescription") String processInstanceDescription,
                   final @MapsTo("imports") Imports imports,
                   final @MapsTo("executable") boolean executable,
                   final @MapsTo("slaDueDate") String slaDueDate,
                   final @MapsTo(PROCESS_DATA) ProcessData processData,
                   final @MapsTo(CASE_MANAGEMENT_SET) CaseManagementSet caseManagementSet,
                   final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                   final @MapsTo("fontSet") FontSet fontSet,
                   final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                   final @MapsTo(ADVANCED_DATA) RootProcessAdvancedData advancedData) {
        this.name = name;
        this.documentation = documentation;
        this.id = id;
        this.packageName = packageName;
        this.processType = processType;
        this.version = version;
        this.adHoc = adHoc;
        this.processInstanceDescription = processInstanceDescription;
        this.imports = imports;
        this.executable = executable;
        this.slaDueDate = slaDueDate;
        this.processData = processData;
        this.caseManagementSet = caseManagementSet;
        this.backgroundSet = backgroundSet;
        this.fontSet = fontSet;
        this.dimensionsSet = dimensionsSet;
        this.advancedData = advancedData;
    }

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
    Execution sets not removed due to how forms works now, should be refactored during the migration
    to the new forms.
     */
    public ExtensionElements getExtensionElements() {
        ExtensionElements elements = new ExtensionElements();
        List<MetaData> metaData = new ArrayList<>();
        elements.setMetaData(metaData);

        if (StringUtils.nonEmpty(this.getProcessInstanceDescription())) {
            MetaData name = new MetaData("customDescription", this.getProcessInstanceDescription());
            metaData.add(name);
        }

        if (StringUtils.nonEmpty(this.getSlaDueDate())) {
            MetaData sla = new MetaData("customSLADueDate", this.getSlaDueDate());
            elements.getMetaData().add(sla);
        }

        ImportsValue imports = getImports().getValue();
        //List<WSDLImport> wsdlImports = imports.getWSDLImports();
        List<DefaultImport> defaultImports = imports.getDefaultImports();
        if (defaultImports != null && !defaultImports.isEmpty()) {
            for (DefaultImport i : defaultImports) {
                elements.getImports().add(new Import(i.getClassName()));
            }
        }

        //GLOBAL identifier type; format is "name:type:,name2:type2:"
        GlobalVariables globalVariables = getAdvancedData().getGlobalVariables();
        if (globalVariables != null
                && globalVariables.getValue() != null
                && !globalVariables.getValue().isEmpty()) {
            String[] globals = globalVariables.getValue().split(",");
            for (String global : globals) {
                String[] g = global.split(":");
                elements.getGlobals().add(new Global(g[0], g[1]));
            }
        }

        metaData.addAll(this.getAdvancedData().getAsMetaData());

        return elements;
    }

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
    Execution sets not removed due to how forms works now, should be refactored during the migration
    to the new forms.
     */
    public void setExtensionElements(ExtensionElements extensionElements) {
        this.extensionElements = extensionElements;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDocumentation() {
        return documentation;
    }

    @Override
    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getProcessType() {
        return processType;
    }

    @Override
    public void setProcessType(String processType) {
        this.processType = processType;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean isAdHoc() {
        return adHoc;
    }

    @Override
    public void setAdHoc(Boolean adHoc) {
        this.adHoc = adHoc;
    }

    @Override
    public String getProcessInstanceDescription() {
        return processInstanceDescription;
    }

    @Override
    public void setProcessInstanceDescription(String processInstanceDescription) {
        this.processInstanceDescription = processInstanceDescription;
    }

    @Override
    public Imports getImports() {
        return imports;
    }

    @Override
    public void setImports(Imports imports) {
        this.imports = imports;
    }

    @Override
    public Boolean isExecutable() {
        return executable;
    }

    @Override
    public void setExecutable(Boolean executable) {
        this.executable = executable;
    }

    @Override
    public String getSlaDueDate() {
        return slaDueDate;
    }

    @Override
    public void setSlaDueDate(String slaDueDate) {
        this.slaDueDate = slaDueDate;
    }

    public String getCategory() {
        return category;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public RectangleDimensionsSet getDimensionsSet() {
        return dimensionsSet;
    }

    public void setDimensionsSet(final RectangleDimensionsSet dimensionsSet) {
        this.dimensionsSet = dimensionsSet;
    }

    @Override
    public ProcessData getProcessData() {
        return processData;
    }

    @Override
    public CaseManagementSet getCaseManagementSet() {
        return caseManagementSet;
    }

    @Override
    public void setCaseManagementSet(final CaseManagementSet caseManagementSet) {
        this.caseManagementSet = caseManagementSet;
    }

    @Override
    public BackgroundSet getBackgroundSet() {
        return backgroundSet;
    }

    @Override
    public FontSet getFontSet() {
        return fontSet;
    }

    @Override
    public void setProcessData(final ProcessData processData) {
        this.processData = processData;
    }

    @Override
    public void setBackgroundSet(final BackgroundSet backgroundSet) {
        this.backgroundSet = backgroundSet;
    }

    @Override
    public void setFontSet(final FontSet fontSet) {
        this.fontSet = fontSet;
    }

    @Override
    public RootProcessAdvancedData getAdvancedData() {
        return advancedData;
    }

    @Override
    public void setAdvancedData(RootProcessAdvancedData advancedData) {
        this.advancedData = advancedData;
    }

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

    public List<org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Property> getProperties() {
        String value = getProcessData().getProcessVariables();
        if (value == null || value.isEmpty()) {
            return null;
        }

        properties.clear();
        properties.addAll(AssignmentParser.parseProcessVariables(value));

        return properties;
    }

    public void setProperties(List<org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Property> properties) {
        this.properties = properties;
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

    public List<DataObject> getDataObjects() {
        return dataObjects;
    }

    public void setDataObjects(List<DataObject> dataObjects) {
        this.dataObjects = dataObjects;
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
        return HashUtil.combineHashCodes(Objects.hashCode(processData),
                                         Objects.hashCode(caseManagementSet),
                                         Objects.hashCode(backgroundSet),
                                         Objects.hashCode(fontSet),
                                         Objects.hashCode(dimensionsSet),
                                         Objects.hashCode(advancedData),
                                         Objects.hashCode(startEvents),
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
                                         Objects.hashCode(callActivities),
                                         Objects.hashCode(adHocSubProcess),
                                         Objects.hashCode(businessRuleTask),
                                         Objects.hashCode(properties));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Process) {
            Process other = (Process) o;
            return Objects.equals(processData, other.processData)
                    && Objects.equals(caseManagementSet, other.caseManagementSet)
                    && Objects.equals(backgroundSet, other.backgroundSet)
                    && Objects.equals(fontSet, other.fontSet)
                    && Objects.equals(dimensionsSet, other.dimensionsSet)
                    && Objects.equals(advancedData, other.advancedData)
                    && Objects.equals(startEvents, other.startEvents)
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
                    && Objects.equals(callActivities, other.callActivities)
                    && Objects.equals(adHocSubProcess, other.adHocSubProcess)
                    && Objects.equals(businessRuleTask, other.businessRuleTask)
                    && Objects.equals(properties, other.properties);
        }
        return false;
    }
}
