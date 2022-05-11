/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FieldParam;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.settings.FieldPolicy;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.core.definition.annotation.Definition;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Labels;

import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.COLLAPSIBLE_CONTAINER;
import static org.kie.workbench.common.forms.adf.engine.shared.formGeneration.processing.fields.fieldInitializers.nestedForms.SubFormFieldInitializer.FIELD_CONTAINER_PARAM;

@Portable
@Bindable
@Definition
@FormDefinition(
        policy = FieldPolicy.ONLY_MARKED,
        startElement = "general",
        defaultFieldSettings = {@FieldParam(name = FIELD_CONTAINER_PARAM, value = COLLAPSIBLE_CONTAINER)}
)
@XmlRootElement(name = "textAnnotation", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class TextAnnotation extends BaseArtifacts {

    @Labels
    private static final Set<String> labels = Stream.of("text_annotation",
                                                        "lane_child",
                                                        "all")
            .collect(Collectors.toSet());

    /*
        Used only for marshalling/unmarshalling purposes. Shouldn't be handled in Equals/HashCode.
        This variable will be always null and getter/setter will return data from other Execution sets.
        Execution sets not removed due to how forms works now, should be refactored during the migration
        to the new forms.
     */
    @XmlElement(name = "text")
    public String text;

    public TextAnnotation() {
        this("Text Annotation",
             "",
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(),
             new AdvancedData());
    }

    public TextAnnotation(final @MapsTo("name") String name,
                          final @MapsTo("documentation") String documentation,
                          final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                          final @MapsTo("fontSet") FontSet fontSet,
                          final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                          final @MapsTo("advancedData") AdvancedData advancedData) {
        super(name, documentation, backgroundSet, fontSet, dimensionsSet, advancedData);
    }

    public String getText() {
        return getName();
    }

    public void setText(String text) {
        setName(text);
    }

    public Set<String> getLabels() {
        return labels;
    }
}