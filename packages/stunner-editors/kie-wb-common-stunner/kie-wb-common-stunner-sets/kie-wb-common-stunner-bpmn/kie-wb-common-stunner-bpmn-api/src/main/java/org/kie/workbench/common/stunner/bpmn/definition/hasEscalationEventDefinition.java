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

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.Escalation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.EscalationEventDefinition;

public interface hasEscalationEventDefinition {

    String getEscalationRefValue();

    String getEscalationId();

    default EscalationEventDefinition getEscalationEventDefinition() {
        return new EscalationEventDefinition(getEscalationRefValue(), getEscalationId());
    }

    void setEscalationEventDefinition(EscalationEventDefinition escalationEventDefinition);

    default Escalation getEscalation() {
        return new Escalation(getEscalationId(),
                              getEscalationRefValue());
    }

}