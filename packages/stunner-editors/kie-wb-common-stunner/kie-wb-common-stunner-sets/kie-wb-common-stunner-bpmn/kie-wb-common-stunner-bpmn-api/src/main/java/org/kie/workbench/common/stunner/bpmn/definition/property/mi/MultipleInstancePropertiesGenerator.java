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
package org.kie.workbench.common.stunner.bpmn.definition.property.mi;

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataInput;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataOutput;

public class MultipleInstancePropertiesGenerator {

    public static final String IN_COLLECTION = "IN_COLLECTION";

    public static final String OUT_COLLECTION = "OUT_COLLECTION";

    private static final String UNDERSCORE = "_";

    private static final String INPUT_X = "InputX";

    private static final String OUTPUT_X = "OutputX";

    public static final String COLLECTION_INPUT = UNDERSCORE + IN_COLLECTION + INPUT_X;

    public static final String COLLECTION_OUTPUT = UNDERSCORE + OUT_COLLECTION + OUTPUT_X;

    private static final String ITEM = "Item";

    private static final String ITEM_TYPE = "_multiInstanceItemType_";

    public static DataInput createInputCollection(String nodeId, String collectionInputId) {
        DataInput inputCollection = new DataInput();
        inputCollection.setName(IN_COLLECTION);
        inputCollection.setId(nodeId + COLLECTION_INPUT);
        inputCollection.setItemSubjectRef(UNDERSCORE + collectionInputId + ITEM);
        return inputCollection;
    }

    public static DataOutput createOutputCollection(String nodeId, String collectionOutputId) {
        DataOutput outputCollection = new DataOutput();
        outputCollection.setName(OUT_COLLECTION);
        outputCollection.setId(nodeId + COLLECTION_OUTPUT);
        outputCollection.setItemSubjectRef(UNDERSCORE + collectionOutputId + ITEM);
        return outputCollection;
    }

    public static DataInput createInput(String nodeId, String inputId) {
        DataInput inputItem = new DataInput();
        inputItem.setName(inputId);
        inputItem.setId(nodeId + UNDERSCORE + inputId + INPUT_X);
        inputItem.setItemSubjectRef(nodeId + ITEM_TYPE + inputId);
        return inputItem;
    }

    public static DataOutput createOutput(String nodeId, String inputId) {
        DataOutput outputItem = new DataOutput();
        outputItem.setName(inputId);
        outputItem.setId(nodeId + UNDERSCORE + inputId + OUTPUT_X);
        outputItem.setItemSubjectRef(nodeId + ITEM_TYPE + inputId);
        return outputItem;
    }
}
