/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
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

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.errai.common.client.api.annotations.MapsTo;

@XmlRootElement(name = "association", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class Association extends BaseConnector {

    // Marshalled in Extensions metadata
    @XmlTransient
    private boolean isAutoConnectionSource = false;

    // Marshalled in Extensions metadata
    @XmlTransient
    private boolean isAutoConnectionTarget = false;

    public Association() {
        this("", "");
    }

    public Association(final @MapsTo("name") String name,
                       final @MapsTo("documentation") String documentation) {
        super(name,
              documentation);
    }

    /*
        Current marshaller and BPMN 2 spec doesn't support Association name.
        BPMN 2 spec doesn't support documentation either but current marshallers does,
        do don't removing documentation from the Association.
     */
    public String getName() {
        return null;
    }

    @Override
    public boolean isAutoConnectionSource() {
        return isAutoConnectionSource;
    }

    @Override
    public void setAutoConnectionSource(boolean autoConnectionSource) {
        isAutoConnectionSource = autoConnectionSource;
    }

    @Override
    public boolean isAutoConnectionTarget() {
        return isAutoConnectionTarget;
    }

    @Override
    public void setAutoConnectionTarget(boolean autoConnectionTarget) {
        isAutoConnectionTarget = autoConnectionTarget;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Association) {
            Association other = (Association) o;
            return super.equals(other);
        }
        return false;
    }
}
