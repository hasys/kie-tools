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

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataOutput;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataOutputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.OutputSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.assignment.AssignmentParser;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;

public interface hasOutputAssignments {

    String getId();

    DataIOSet getDataIOSet();

    default List<DataOutput> getDataOutputs() {
        return AssignmentParser.parseDataOutputs(getId(), getDataIOSet().getAssignmentsinfo().getValue());
    }

    void setDataOutputs(List<DataOutput> dataOutputs);

    default List<DataOutputAssociation> getDataOutputAssociation() {
        return AssignmentParser.parseDataOutputAssociation(getId(), getDataIOSet().getAssignmentsinfo().getValue());
    }

    void setDataOutputAssociation(List<DataOutputAssociation> dataOutputAssociation);

    default List<OutputSet> getOutputSet() {
        return AssignmentParser.getOutputSet(getId(), getDataIOSet().getAssignmentsinfo().getValue());
    }

    void setOutputSet(List<OutputSet> outputSets);
}