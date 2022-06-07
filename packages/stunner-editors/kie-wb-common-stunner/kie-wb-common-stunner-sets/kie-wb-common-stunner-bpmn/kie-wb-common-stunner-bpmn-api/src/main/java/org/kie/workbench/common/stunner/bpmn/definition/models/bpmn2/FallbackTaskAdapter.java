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

public class FallbackTaskAdapter extends XmlAdapter<FallbackTask, BaseTask> {

    @Override
    public BaseTask unmarshal(FallbackTask v) {
        BaseTask result;

        if (v.getTaskName() != null) {
            CustomTask customTask = new CustomTask();
            customTask.setTaskName(v.getTaskName());
            customTask.setIoSpecification(v.getIoSpecification());
            customTask.setDataInputAssociation(v.getDataInputAssociation());
            customTask.setDataOutputAssociation(v.getDataOutputAssociation());
            result = customTask;
        } else {
            result = new NoneTask();
        }

        FallbackFlowNodeAdapter.unmarshal(result, v);

        return result;
    }

    @Override
    public FallbackTask marshal(BaseTask v) {
        FallbackTask fallbackTask = new FallbackTask();
        fallbackTask.setId(v.getId());
        fallbackTask.setName(v.getName());
        fallbackTask.setDocumentation(v.getDocumentation());
        fallbackTask.setExtensionElements(v.getExtensionElements());

        if (v instanceof CustomTask) {
            CustomTask customTask = (CustomTask) v;
            fallbackTask.setTaskName(customTask.getTaskName());
            fallbackTask.setIoSpecification(customTask.getIoSpecification());
            fallbackTask.setDataInputAssociation(customTask.getDataInputAssociation());
            fallbackTask.setDataOutputAssociation(customTask.getDataOutputAssociation());
        }

        return fallbackTask;
    }
}