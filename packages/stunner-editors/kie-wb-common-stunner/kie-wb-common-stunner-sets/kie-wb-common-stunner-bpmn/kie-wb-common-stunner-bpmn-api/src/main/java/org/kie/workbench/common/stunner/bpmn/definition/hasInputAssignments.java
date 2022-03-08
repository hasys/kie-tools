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
package org.kie.workbench.common.stunner.bpmn.definition;

import java.util.List;

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataInput;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataInputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.InputSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;

public interface hasInputAssignments {

    String getId();

    DataIOSet getDataIOSet();

    default List<DataInput> getDataInputs() {
        return AssignmentParser.parseDataInputs(getId(), getDataIOSet().getAssignmentsinfo().getValue());
    }

    void setDataInputs(List<DataInput> dataInputs);

    default List<DataInputAssociation> getDataInputAssociation() {
        return AssignmentParser.parseDataInputAssociation(getId(), getDataIOSet().getAssignmentsinfo().getValue());
    }

    void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation);

    default List<InputSet> getInputSet() {
        return AssignmentParser.getInputSet(getId(), getDataIOSet().getAssignmentsinfo().getValue());
    }

    void setInputSet(List<InputSet> inputSet);
}
