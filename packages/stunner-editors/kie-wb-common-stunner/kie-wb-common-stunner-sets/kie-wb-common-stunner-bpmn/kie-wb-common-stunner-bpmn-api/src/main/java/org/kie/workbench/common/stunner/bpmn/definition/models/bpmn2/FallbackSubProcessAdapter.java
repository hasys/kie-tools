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

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FallbackSubProcessAdapter extends XmlAdapter<FallbackSubProcess, BaseSubprocess> {

    @Override
    public BaseSubprocess unmarshal(FallbackSubProcess v) {
        BaseSubprocess result;

        if (v.getTriggeredByEvent() != null) {
            EventSubprocess customTask = new EventSubprocess();
            customTask.setTriggeredByEvent(v.getTriggeredByEvent());
            result = customTask;
        } else if (v.getMultiInstanceLoopCharacteristics() != null) {
            MultipleInstanceSubprocess multipleInstanceSubprocess = new MultipleInstanceSubprocess();
            multipleInstanceSubprocess.setIoSpecification(v.getIoSpecification());
            multipleInstanceSubprocess.setDataInputAssociation(v.getDataInputAssociation());
            multipleInstanceSubprocess.setDataOutputAssociation(v.getDataOutputAssociation());
            multipleInstanceSubprocess.setMultiInstanceLoopCharacteristics(v.getMultiInstanceLoopCharacteristics());
            result = multipleInstanceSubprocess;
        } else {
            result = new EmbeddedSubprocess();
        }

        result.setId(v.getId());
        result.setName(v.getName());
        result.setDocumentation(v.getDocumentation());
        result.getAdvancedData().setMetaDataAttributes(v.getAdvancedData().getMetaDataAttributes());

        return result;
    }

    @Override
    public FallbackSubProcess marshal(BaseSubprocess v) {
        FallbackSubProcess fallbackSubProcess = new FallbackSubProcess();
        fallbackSubProcess.setId(v.getId());
        fallbackSubProcess.setName(v.getName());
        fallbackSubProcess.setDocumentation(v.getDocumentation());
        fallbackSubProcess.setExtensionElements(v.getExtensionElements());
        fallbackSubProcess.setProperties(v.getProperties());

        if (v instanceof EventSubprocess) {
            EventSubprocess eventSubprocess = (EventSubprocess) v;
            fallbackSubProcess.setTriggeredByEvent(eventSubprocess.getTriggeredByEvent());
        } else if (v instanceof MultipleInstanceSubprocess) {
            MultipleInstanceSubprocess multipleInstanceSubprocess = (MultipleInstanceSubprocess) v;
            fallbackSubProcess.setIoSpecification(multipleInstanceSubprocess.getIoSpecification());
            fallbackSubProcess.setDataInputAssociation(multipleInstanceSubprocess.getDataInputAssociation());
            fallbackSubProcess.setDataOutputAssociation(multipleInstanceSubprocess.getDataOutputAssociation());
            fallbackSubProcess.setMultiInstanceLoopCharacteristics(multipleInstanceSubprocess.getMultiInstanceLoopCharacteristics());
        }

        return fallbackSubProcess;
    }
}