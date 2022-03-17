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
import org.kie.workbench.common.stunner.bpmn.definition.hasTimerEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.timer.InterruptingTimerEventExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.timer.TimerSettingsValue;
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
@Morph(base = StartEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "startEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class StartTimerEvent extends StartEvent implements hasCustomSLADueDate,
                                                           hasTimerEventDefinition {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    protected InterruptingTimerEventExecutionSet executionSet;

    public TimerEventDefinition timerEventDefinition;

    public StartTimerEvent() {
        this("",
             "",
             new AdvancedData(),
             new InterruptingTimerEventExecutionSet());
    }

    public StartTimerEvent(final @MapsTo("name") String name,
                           final @MapsTo("documentation") String documentation,
                           final @MapsTo("advancedData") AdvancedData advancedData,
                           final @MapsTo("executionSet") InterruptingTimerEventExecutionSet executionSet) {
        super(name,
              documentation,
              advancedData);
        this.executionSet = executionSet;
    }

    public TimerSettingsValue getTimerSettingsValue() {
        return executionSet.getTimerSettings().getValue();
    }

    public TimerEventDefinition getTimerEventDefinition() {
        return hasTimerEventDefinition.super.getTimerEventDefinition();
    }

    public void setTimerEventDefinition(TimerEventDefinition timerEventDefinition) {
        this.timerEventDefinition = timerEventDefinition;
    }

    public InterruptingTimerEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(final InterruptingTimerEventExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    public ExtensionElements getSuperExtensionElements() {
        return super.getExtensionElements();
    }

    public String getSlaDueDateString() {
        return executionSet.getSlaDueDate();
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
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         executionSet.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StartTimerEvent) {
            StartTimerEvent other = (StartTimerEvent) o;
            return super.equals(other) &&
                    executionSet.equals(other.executionSet);
        }
        return false;
    }
}
