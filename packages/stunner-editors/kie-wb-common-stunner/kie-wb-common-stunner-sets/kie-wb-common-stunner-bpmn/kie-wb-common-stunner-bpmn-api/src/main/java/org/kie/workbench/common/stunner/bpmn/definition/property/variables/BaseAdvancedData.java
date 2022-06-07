/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.definition.property.variables;

import java.util.ArrayList;
import java.util.List;

import org.kie.workbench.common.stunner.bpmn.definition.BPMNPropertySet;
import org.kie.workbench.common.stunner.bpmn.definition.models.drools.MetaData;
import org.kie.workbench.common.stunner.core.util.StringUtils;

public interface BaseAdvancedData extends BPMNPropertySet {

    String getMetaDataAttributes();

    void setMetaDataAttributes(String metaData);

    default List<MetaData> getAsMetaData() {
        List<MetaData> metaData = new ArrayList<>();

        if (StringUtils.nonEmpty(getMetaDataAttributes())) {
            String[] metaArray = getMetaDataAttributes().split("Ø");
            for (String md : metaArray) {
                String[] metaNV = md.split("ß");
                MetaData meta = new MetaData(metaNV[0], metaNV[1]);
                metaData.add(meta);
            }
        }

        return metaData;
    }

    default void setMetaDataAttributes(List<MetaData> metaDataList) {
        StringBuilder builder = new StringBuilder();
        for (MetaData metaData : metaDataList) {
            if (!metaData.getName().equals("elementname")
                    && !metaData.getName().equals("customSLADueDate")
                    && !metaData.getName().equals("customAsync")
                    && !metaData.getName().equals("customAutoStart")) {
                String variable = metaData.getName() + "ß" + metaData.getMetaValue();
                builder.append(variable);
                builder.append("Ø");
            }
        }
        if (builder.length() > 0) {
            setMetaDataAttributes(builder.substring(0, builder.length() - 1));
        }
    }
}