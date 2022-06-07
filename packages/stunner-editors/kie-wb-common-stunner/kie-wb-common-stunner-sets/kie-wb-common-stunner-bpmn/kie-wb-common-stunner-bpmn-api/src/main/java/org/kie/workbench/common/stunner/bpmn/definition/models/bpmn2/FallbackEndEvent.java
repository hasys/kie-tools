/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;

import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.error.ErrorRef;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.escalation.EscalationRef;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.message.MessageRef;
import org.kie.workbench.common.stunner.bpmn.definition.property.event.signal.SignalRef;
import org.treblereel.gwt.xml.mapper.api.annotation.XMLMapper;

@XMLMapper
public class FallbackEndEvent extends EndEvent {

    @XmlElementRefs({
            @XmlElementRef(name = "compensateEventDefinition", type = CompensateEventDefinition.class),
            @XmlElementRef(name = "messageEventDefinition", type = MessageEventDefinition.class),
            @XmlElementRef(name = "signalEventDefinition", type = SignalEventDefinition.class),
            @XmlElementRef(name = "errorEventDefinition", type = ErrorEventDefinition.class),
            @XmlElementRef(name = "escalationEventDefinition", type = EscalationEventDefinition.class),
            @XmlElementRef(name = "terminateEventDefinition", type = TerminateEventDefinition.class),
    })
    private AbstractEventDefinition eventDefinition;

    @XmlElement(name = "dataInput")
    private List<DataInput> dataInputs;

    @XmlElement(name = "dataInputAssociation")
    private List<DataInputAssociation> dataInputAssociation;

    @XmlElement(name = "inputSet")
    private List<InputSet> inputSet;

    private FallbackEndEventExecutionSet executionSet;

    private DataIOSet dataIOSet;

    public AbstractEventDefinition getEventDefinition() {
        return eventDefinition;
    }

    public ExtensionElements getExtensionElements() {
        return super.getFullExtensionElements();
    }

    public void setEventDefinition(AbstractEventDefinition eventDefinition) {
        this.eventDefinition = eventDefinition;
    }

    public List<DataInput> getDataInputs() {
        return dataInputs;
    }

    public void setDataInputs(List<DataInput> dataInputs) {
        this.dataInputs = dataInputs;
    }

    public List<DataInputAssociation> getDataInputAssociation() {
        return dataInputAssociation;
    }

    public void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation) {
        this.dataInputAssociation = dataInputAssociation;
    }

    public List<InputSet> getInputSet() {
        return inputSet;
    }

    public void setInputSet(List<InputSet> inputSet) {
        this.inputSet = inputSet;
    }

    public FallbackEndEventExecutionSet getExecutionSet() {
        return executionSet;
    }

    public void setExecutionSet(FallbackEndEventExecutionSet executionSet) {
        this.executionSet = executionSet;
    }

    public DataIOSet getDataIOSet() {
        return dataIOSet;
    }

    public void setDataIOSet(DataIOSet dataIOSet) {
        this.dataIOSet = dataIOSet;
    }

    public static class FallbackEndEventExecutionSet {

        private Boolean isInterrupting;

        private String metaData;

        private ErrorRef errorRef;

        private EscalationRef escalationRef;

        private SignalRef signalRef;

        private MessageRef messageRef;

        public Boolean getIsInterrupting() {
            return isInterrupting;
        }

        public void setIsInterrupting(Boolean interrupting) {
            isInterrupting = interrupting;
        }

        public String getMetaData() {
            return metaData;
        }

        public void setMetaData(String metaData) {
            this.metaData = metaData;
        }

        public ErrorRef getErrorRef() {
            return errorRef;
        }

        public void setErrorRef(ErrorRef errorRef) {
            this.errorRef = errorRef;
        }

        public EscalationRef getEscalationRef() {
            return escalationRef;
        }

        public void setEscalationRef(EscalationRef escalationRef) {
            this.escalationRef = escalationRef;
        }

        public SignalRef getSignalRef() {
            return signalRef;
        }

        public void setSignalRef(SignalRef signalRef) {
            this.signalRef = signalRef;
        }

        public MessageRef getMessageRef() {
            return messageRef;
        }

        public void setMessageRef(MessageRef messageRef) {
            this.messageRef = messageRef;
        }
    }
}