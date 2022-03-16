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

import java.util.Objects;

import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FieldParam;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.forms.adf.definitions.settings.FieldPolicy;
import org.kie.workbench.common.stunner.bpmn.definition.hasCustomSLADueDate;
import org.kie.workbench.common.stunner.bpmn.definition.hasMessageEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.collaboration.events.CorrelationModel;
import org.kie.workbench.common.stunner.bpmn.definition.property.collaboration.events.CorrelationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.CircleDimensionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.message.CancellingMessageEventExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.util.HashUtil;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@Morph(base = BaseCatchingIntermediateEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "intermediateCatchEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class IntermediateMessageEventCatching
        extends BaseCatchingIntermediateEvent
        implements CorrelationModel,
                   hasMessageEventDefinition,
                   hasCustomSLADueDate {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    protected CancellingMessageEventExecutionSet executionSet;

    @XmlTransient
    private String messageId;

    public MessageEventDefinition messageEventDefinition;

    @Property
    @FormField(afterElement = "advancedData")
    @Valid
    protected CorrelationSet correlationSet;

    public IntermediateMessageEventCatching() {
        this("",
             "",
             new BackgroundSet(),
             new FontSet(),
             new CircleDimensionSet(),
             new DataIOSet(),
             new AdvancedData(),
             new CorrelationSet(),
             new CancellingMessageEventExecutionSet());
    }

    public IntermediateMessageEventCatching(final @MapsTo("name") String name,
                                            final @MapsTo("documentation") String documentation,
                                            final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                                            final @MapsTo("fontSet") FontSet fontSet,
                                            final @MapsTo("dimensionsSet") CircleDimensionSet dimensionsSet,
                                            final @MapsTo("dataIOSet") DataIOSet dataIOSet,
                                            final @MapsTo("advancedData") AdvancedData advancedData,
                                            final @MapsTo("correlationSet") CorrelationSet correlationSet,
                                            final @MapsTo("executionSet") CancellingMessageEventExecutionSet executionSet) {
        super(name,
              documentation,
              backgroundSet,
              fontSet,
              dimensionsSet,
              dataIOSet,
              advancedData);
        this.correlationSet = correlationSet;
        this.executionSet = executionSet;
    }

    public String getMessageRefValue() {
        return executionSet.getMessageRef().getValue();
    }

    public MessageEventDefinition getMessageEventDefinition() {
        return hasMessageEventDefinition.super.getMessageEventDefinition();
    }

    public void setMessageEventDefinition(MessageEventDefinition messageEventDefinition) {
        this.messageEventDefinition = messageEventDefinition;
    }

    public Message getMessage() {
        return hasMessageEventDefinition.super.getMessage();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public CancellingMessageEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(CancellingMessageEventExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    public CorrelationSet getCorrelationSet() {
        return correlationSet;
    }

    public void setCorrelationSet(CorrelationSet correlationSet) {
        this.correlationSet = correlationSet;
    }

    public ExtensionElements getSuperExtensionElements() {
        return super.getExtensionElements();
    }

    public String getSlaDueDateString() {
        return executionSet.getSlaDueDate().getValue();
    }

    /*
     Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
     Variable is not used and always null. Getters/setters redirect data from other execution sets.
     Execution sets not removed due to how forms works now, should be refactored during the migration
     to the new forms.
      */
    @Override
    public ExtensionElements getExtensionElements() {
        return hasCustomSLADueDate.super.getExtensionElements();
    }

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
    Variable is not used and always null. Getters/setters redirect data from other execution sets.
    Execution sets not removed due to how forms works now, should be refactored during the migration
    to the new forms.
     */
    @Override
    public void setExtensionElements(ExtensionElements extensionElements) {
        super.setExtensionElements(extensionElements);
    }

    @Override
    protected void initLabels() {
        super.initLabels();
        labels.add("messageflow_end");
        labels.add("FromEventbasedGateway");
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode(),
                                         correlationSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntermediateMessageEventCatching) {
            IntermediateMessageEventCatching other = (IntermediateMessageEventCatching) o;
            return super.equals(other) &&
                    Objects.equals(executionSet, other.executionSet) &&
                    Objects.equals(correlationSet, other.correlationSet);
        }
        return false;
    }
}