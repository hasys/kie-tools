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

package org.kie.workbench.common.stunner.bpmn.definition.property.task;

import java.util.Objects;

import javax.validation.Valid;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.jboss.errai.common.client.api.annotations.Portable;
import org.jboss.errai.databinding.client.api.Bindable;
import org.kie.workbench.common.forms.adf.definitions.annotations.FieldParam;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormDefinition;
import org.kie.workbench.common.forms.adf.definitions.annotations.FormField;
import org.kie.workbench.common.forms.adf.definitions.annotations.field.selector.SelectorDataProvider;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.selectors.listBox.type.ListBoxFieldType;
import org.kie.workbench.common.forms.fields.shared.fieldTypes.basic.textArea.type.TextAreaFieldType;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.ExtensionElements;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.bpmn.definition.property.general.SLADueDate;
import org.kie.workbench.common.stunner.bpmn.definition.property.subProcess.execution.EmbeddedSubprocessExecutionSet;
import org.kie.workbench.common.stunner.core.definition.annotation.Property;
import org.kie.workbench.common.stunner.core.util.HashUtil;

@Portable
@Bindable
@FormDefinition(startElement = "adHocActivationCondition")
public class AdHocSubprocessTaskExecutionSet
        extends EmbeddedSubprocessExecutionSet
        implements BaseAdHocSubprocessTaskExecutionSet {

    @Property
    @FormField(type = TextAreaFieldType.class,
            settings = {@FieldParam(name = "rows", value = "5")})
    @Valid
    private AdHocActivationCondition adHocActivationCondition;

    @Property
    @FormField(afterElement = "adHocActivationCondition",
            settings = {@FieldParam(name = "mode", value = "COMPLETION_CONDITION")})
    @Valid
    private AdHocCompletionCondition adHocCompletionCondition;

    @Property
    @FormField(afterElement = "adHocCompletionCondition",
            type = ListBoxFieldType.class,
            settings = {@FieldParam(name = "addEmptyOption", value = "false")}
    )
    @SelectorDataProvider(
            type = SelectorDataProvider.ProviderType.CLIENT,
            className = "org.kie.workbench.common.stunner.bpmn.client.dataproviders.ExecutionOrderProvider")
    @Valid
    private AdHocOrdering adHocOrdering;

    @Property
    @FormField(afterElement = "adHocOrdering")
    @Valid
    private AdHocAutostart adHocAutostart;

    public AdHocSubprocessTaskExecutionSet() {
        this(new AdHocActivationCondition(),
             new AdHocCompletionCondition(new ScriptTypeValue("mvel", "autocomplete")),
             new AdHocOrdering("Sequential"),
             new AdHocAutostart(),
             new OnEntryAction(new ScriptTypeListValue().addValue(new ScriptTypeValue("java", ""))),
             new OnExitAction(new ScriptTypeListValue().addValue(new ScriptTypeValue("java", ""))),
             new IsAsync(),
             new SLADueDate());
    }

    public AdHocSubprocessTaskExecutionSet(final @MapsTo("adHocActivationCondition") AdHocActivationCondition adHocActivationCondition,
                                           final @MapsTo("adHocCompletionCondition") AdHocCompletionCondition adHocCompletionCondition,
                                           final @MapsTo("adHocOrdering") AdHocOrdering adHocOrdering,
                                           final @MapsTo("adHocAutostart") AdHocAutostart adHocAutostart,
                                           final @MapsTo("onEntryAction") OnEntryAction onEntryAction,
                                           final @MapsTo("onExitAction") OnExitAction onExitAction,
                                           final @MapsTo("isAsync") IsAsync isAsync,
                                           final @MapsTo("slaDueDate") SLADueDate slaDueDate) {
        super(onEntryAction, onExitAction, isAsync, slaDueDate);
        this.adHocActivationCondition = adHocActivationCondition;
        this.adHocCompletionCondition = adHocCompletionCondition;
        this.adHocOrdering = adHocOrdering;
        this.adHocAutostart = adHocAutostart;
    }

    @Override
    public AdHocActivationCondition getAdHocActivationCondition() {
        return adHocActivationCondition;
    }

    public void setAdHocActivationCondition(AdHocActivationCondition adHocActivationCondition) {
        this.adHocActivationCondition = adHocActivationCondition;
    }

    @Override
    public AdHocCompletionCondition getAdHocCompletionCondition() {
        return adHocCompletionCondition;
    }

    public void setAdHocCompletionCondition(AdHocCompletionCondition adHocCompletionCondition) {
        this.adHocCompletionCondition = adHocCompletionCondition;
    }

    @Override
    public AdHocOrdering getAdHocOrdering() {
        return adHocOrdering;
    }

    public void setAdHocOrdering(AdHocOrdering adHocOrdering) {
        this.adHocOrdering = adHocOrdering;
    }

    @Override
    public AdHocAutostart getAdHocAutostart() {
        return adHocAutostart;
    }

    public void setAdHocAutostart(AdHocAutostart adHocAutostart) {
        this.adHocAutostart = adHocAutostart;
    }

    public void setActivationConditionMetadata(ExtensionElements elements) {
        if (adHocActivationCondition != null
                && adHocActivationCondition.getValue() != null
                && !adHocActivationCondition.getValue().isEmpty()) {
            MetaData activationCondition = new MetaData("customActivationCondition",
                                                        adHocActivationCondition.getValue());
            elements.getMetaData().add(activationCondition);
        }
    }

    public void setAutostartMetadata(ExtensionElements elements) {
        if (adHocAutostart != null
                && adHocAutostart.getValue() != null) {
            MetaData autoStart = new MetaData("customAutoStart",
                                              adHocAutostart.getValue().toString());
            elements.getMetaData().add(autoStart);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof AdHocSubprocessTaskExecutionSet) {
            AdHocSubprocessTaskExecutionSet other = (AdHocSubprocessTaskExecutionSet) o;
            return super.equals(other) &&
                    Objects.equals(adHocActivationCondition, other.adHocActivationCondition) &&
                    Objects.equals(adHocCompletionCondition, other.adHocCompletionCondition) &&
                    Objects.equals(adHocOrdering, other.adHocOrdering) &&
                    Objects.equals(adHocAutostart, other.adHocAutostart);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         Objects.hashCode(adHocActivationCondition),
                                         Objects.hashCode(adHocCompletionCondition),
                                         Objects.hashCode(adHocOrdering),
                                         Objects.hashCode(adHocAutostart));
    }
}
