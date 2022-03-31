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
import org.kie.workbench.common.stunner.bpmn.definition.property.event.compensation.CompensationEventExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.compensation.HasActivityRef;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.Morph;
import org.kie.workbench.common.stunner.core.util.HashUtil;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.AbstractEmbeddedFormsInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@Morph(base = EndEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "endEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class EndCompensationEvent extends EndEvent implements HasActivityRef {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    private CompensationEventExecutionSet executionSet;

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
    */
    private CompensateEventDefinition compensateEventDefinition = new CompensateEventDefinition();

    public EndCompensationEvent() {
        this("",
             "",
             new AdvancedData(),
             new CompensationEventExecutionSet());
    }

    public EndCompensationEvent(final @MapsTo("name") String name,
                                final @MapsTo("documentation") String documentation,
                                final @MapsTo("advancedData") AdvancedData advancedData,
                                final @MapsTo("executionSet") CompensationEventExecutionSet executionSet) {
        super(name,
              documentation,
              advancedData);
        this.executionSet = executionSet;
    }

    public CompensationEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(CompensationEventExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
    */
    public CompensateEventDefinition getCompensateEventDefinition() {
        compensateEventDefinition.setActivityRef(executionSet.getActivityRef().getValue());
        return compensateEventDefinition;
    }

    /*
    Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
     */
    public void setCompensateEventDefinition(CompensateEventDefinition compensateEventDefinition) {
        this.compensateEventDefinition = compensateEventDefinition;
    }

    @Override
    public void updateActivityRef(String newActivityRef) {
        executionSet.getActivityRef().setValue(newActivityRef);
    }

    @Override
    public String getActivityRef() {
        return executionSet.getActivityRef().getValue();
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         Objects.hashCode(executionSet));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof EndCompensationEvent) {
            EndCompensationEvent other = (EndCompensationEvent) o;
            return super.equals(other) &&
                    Objects.equals(executionSet,
                                   other.executionSet);
        }
        return false;
    }
}
