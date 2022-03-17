/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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
import org.kie.workbench.common.stunner.bpmn.definition.hasLinkEventDefinition;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.CircleDimensionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.link.LinkEventExecutionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
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
@Morph(base = BaseThrowingIntermediateEvent.class)
@FormDefinition(
        startElement = "name",
        policy = FieldPolicy.ONLY_MARKED,
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "intermediateThrowEvent", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class IntermediateLinkEventThrowing extends BaseThrowingIntermediateEvent implements hasLinkEventDefinition {

    @Property
    @FormField(afterElement = "documentation")
    @Valid
    @XmlTransient
    private LinkEventExecutionSet executionSet;

    public LinkEventDefinition linkEventDefinition;

    public IntermediateLinkEventThrowing() {
        this("",
             "",
             new BackgroundSet(),
             new FontSet(),
             new CircleDimensionSet(),
             new DataIOSet(),
             new AdvancedData(),
             new LinkEventExecutionSet());
    }

    public IntermediateLinkEventThrowing(final @MapsTo("name") String name,
                                         final @MapsTo("documentation") String documentation,
                                         final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                                         final @MapsTo("fontSet") FontSet fontSet,
                                         final @MapsTo("dimensionsSet") CircleDimensionSet dimensionsSet,
                                         final @MapsTo("dataIOSet") DataIOSet dataIOSet,
                                         final @MapsTo("advancedData") AdvancedData advancedData,
                                         final @MapsTo("executionSet") LinkEventExecutionSet executionSet) {
        super(name,
              documentation,
              backgroundSet,
              fontSet,
              dimensionsSet,
              dataIOSet,
              advancedData);
        this.executionSet = executionSet;
    }

    public String getLinkRefvalue() {
        return executionSet.getLinkRef().getValue();
    }

    public LinkEventDefinition getLinkEventDefinition() {
        return hasLinkEventDefinition.super.getLinkEventDefinition();
    }

    public void setLinkEventDefinition(LinkEventDefinition linkEventDefinition) {
        this.linkEventDefinition = linkEventDefinition;
    }

    @Override
    protected void initLabels() {
        super.initLabels();
        // Link Throw Event can't have outgoing connection
        labels.add("Endevents_all");
    }

    public LinkEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(LinkEventExecutionSet executionSet) {
        this.executionSet = executionSet;
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
        if (o instanceof IntermediateLinkEventThrowing) {
            IntermediateLinkEventThrowing other = (IntermediateLinkEventThrowing) o;
            return super.equals(other)
                    && Objects.equals(executionSet, other.executionSet);
        }
        return false;
    }
}
