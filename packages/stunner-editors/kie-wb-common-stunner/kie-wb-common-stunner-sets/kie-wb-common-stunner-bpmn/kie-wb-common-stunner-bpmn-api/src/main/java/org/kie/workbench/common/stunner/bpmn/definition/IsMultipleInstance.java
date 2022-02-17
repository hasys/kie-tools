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
package org.kie.workbench.common.stunner.bpmn.definition;

import java.util.ArrayList;
import java.util.List;

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataInput;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataInputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataInputRefs;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataOutput;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataOutputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataOutputRefs;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.InputDataItem;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.IoSpecification;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.MultiInstanceLoopCharacteristics;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.OutputDataItem;
import org.kie.workbench.common.stunner.bpmn.definition.property.mi.MultipleInstancePropertiesGenerator;

public interface IsMultipleInstance {

    String getId();

    boolean isMultipleInstance();

    String getMultipleInstanceCollectionInputName();

    String getMultipleInstanceDataInputName();

    String getMultipleInstanceCollectionOutputName();

    String getMultipleInstanceDataOutputName();

    String getMultipleInstanceCompletionCondition();

    default IoSpecification getIoSpecification() {
        if (getId() == null) {
            return null;
        }
        IoSpecification ioSpecification = new IoSpecification();

        if (isMultipleInstance()) {
            DataInput inputCollection = MultipleInstancePropertiesGenerator.createInputCollection(getId(), getMultipleInstanceCollectionInputName());
            ioSpecification.getDataInput().add(inputCollection);
            ioSpecification.getInputSet().getDataInputRefs().add(new DataInputRefs(inputCollection.getId()));

            DataInput inputItem = MultipleInstancePropertiesGenerator.createInput(getId(), getMultipleInstanceDataInputName());
            ioSpecification.getDataInput().add(inputItem);
            ioSpecification.getInputSet().getDataInputRefs().add(new DataInputRefs(inputItem.getId()));

            DataOutput outputCollection = MultipleInstancePropertiesGenerator.createOutputCollection(getId(), getMultipleInstanceCollectionOutputName());
            ioSpecification.getDataOutput().add(outputCollection);
            ioSpecification.getOutputSet().getDataOutputRefs().add(new DataOutputRefs(outputCollection.getId()));

            DataOutput outputItem = MultipleInstancePropertiesGenerator.createOutput(getId(), getMultipleInstanceDataOutputName());
            ioSpecification.getDataOutput().add(outputItem);
            ioSpecification.getOutputSet().getDataOutputRefs().add(new DataOutputRefs(outputItem.getId()));
        }

        return ioSpecification;
    }

    default List<DataInputAssociation> getDataInputAssociation() {
        List<DataInputAssociation> dataInputAssociation = new ArrayList<>();

        if (getId() != null && isMultipleInstance()) {
            DataInputAssociation miInputCollection = new DataInputAssociation(getId() + "_IN_COLLECTIONInputX", getMultipleInstanceCollectionInputName());
            dataInputAssociation.add(miInputCollection);

            DataInputAssociation miInputAssociation = new DataInputAssociation(getId() + "_" + getMultipleInstanceDataInputName() + "InputX",
                                                                               getMultipleInstanceDataInputName());
            dataInputAssociation.add(miInputAssociation);
        }
        return dataInputAssociation;
    }

    default List<DataOutputAssociation> getDataOutputAssociation() {
        List<DataOutputAssociation> dataOutputAssociation = new ArrayList<>();

        if (getId() != null && isMultipleInstance()) {
            DataOutputAssociation miOutCollection = new DataOutputAssociation(getId() + "_OUT_COLLECTIONOutputX", getMultipleInstanceCollectionOutputName());
            dataOutputAssociation.add(miOutCollection);

            DataOutputAssociation miOutputAssociation = new DataOutputAssociation(getId() + "_" + getMultipleInstanceDataOutputName() + "OutputX",
                                                                                  getMultipleInstanceDataOutputName());
            dataOutputAssociation.add(miOutputAssociation);
        }
        return dataOutputAssociation;
    }

    default MultiInstanceLoopCharacteristics getMultiInstanceLoopCharacteristics() {
        if (getId() == null || !isMultipleInstance()) {
            return null;
        }

        MultiInstanceLoopCharacteristics multiInstanceLoopCharacteristics = new MultiInstanceLoopCharacteristics();
        multiInstanceLoopCharacteristics.setLoopDataInputRef(getId() + "_IN_COLLECTIONInputX");
        multiInstanceLoopCharacteristics.setLoopDataOutputRef(getId() + "_OUT_COLLECTIONOutputX");
        multiInstanceLoopCharacteristics.getCompletionCondition().setValue(getMultipleInstanceCompletionCondition());
        multiInstanceLoopCharacteristics.setInputDataItem(new InputDataItem(
                getMultipleInstanceDataInputName(),
                getId() + "_multiInstanceItemType_" + getMultipleInstanceDataInputName(),
                getMultipleInstanceDataInputName()
        ));
        multiInstanceLoopCharacteristics.setOutputDataItem(new OutputDataItem(
                getMultipleInstanceDataOutputName(),
                getId() + "_multiInstanceItemType_" + getMultipleInstanceDataOutputName(),
                getMultipleInstanceDataOutputName()
        ));

        return multiInstanceLoopCharacteristics;
    }
}
