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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.treblereel.gwt.xml.mapper.api.annotation.XMLMapper;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

@XMLMapper
public class FallbackTask extends BaseTask {

    @XmlAttribute(namespace = "http://www.jboss.org/drools")
    private String taskName;

    @XmlElement
    private IoSpecification ioSpecification;

    @XmlElement(name = "dataInputAssociation")
    private List<DataInputAssociation> dataInputAssociation;

    @XmlElement(name = "dataOutputAssociation")
    @XmlUnwrappedCollection
    private List<DataOutputAssociation> dataOutputAssociation = new ArrayList<>();

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ExtensionElements getExtensionElements() {
        return super.getFullExtensionElements();
    }

    public List<DataInputAssociation> getDataInputAssociation() {
        return dataInputAssociation;
    }

    public void setDataInputAssociation(List<DataInputAssociation> dataInputAssociation) {
        this.dataInputAssociation = dataInputAssociation;
    }

    public IoSpecification getIoSpecification() {
        return ioSpecification;
    }

    public void setIoSpecification(IoSpecification ioSpecification) {
        this.ioSpecification = ioSpecification;
    }

    public List<DataOutputAssociation> getDataOutputAssociation() {
        return dataOutputAssociation;
    }

    public void setDataOutputAssociation(List<DataOutputAssociation> dataOutputAssociation) {
        this.dataOutputAssociation = dataOutputAssociation;
    }
}