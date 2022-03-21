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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jboss.errai.common.client.api.annotations.MapsTo;
import org.kie.workbench.common.stunner.bpmn.definition.HasIncoming;
import org.kie.workbench.common.stunner.bpmn.definition.HasOutgoing;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOModel;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.ProcessData;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

public abstract class ConnectedBaseSubprocess extends BaseContainerSubprocess implements DataIOModel,
                                                                                         HasOutgoing,
                                                                                         HasIncoming {

    @XmlUnwrappedCollection
    private List<Incoming> incoming = new ArrayList<>();

    @XmlUnwrappedCollection
    private List<Outgoing> outgoing = new ArrayList<>();

    public ConnectedBaseSubprocess() {
        this("Sub-process",
             "",
             new BackgroundSet(),
             new FontSet(),
             new RectangleDimensionsSet(),
             new SimulationSet(),
             new ProcessData(),
             new AdvancedData());
    }

    public ConnectedBaseSubprocess(final @MapsTo("name") String name,
                                   final @MapsTo("documentation") String documentation,
                                   final @MapsTo("backgroundSet") BackgroundSet backgroundSet,
                                   final @MapsTo("fontSet") FontSet fontSet,
                                   final @MapsTo("dimensionsSet") RectangleDimensionsSet dimensionsSet,
                                   final @MapsTo("simulationSet") SimulationSet simulationSet,
                                   final @MapsTo("processData") ProcessData processData,
                                   final @MapsTo("advancedData") AdvancedData advancedData) {
        super(name,
              documentation,
              backgroundSet,
              fontSet,
              dimensionsSet,
              simulationSet,
              advancedData,
              processData);
    }

    public List<Incoming> getIncoming() {
        return incoming;
    }

    public void setIncoming(List<Incoming> incoming) {
        this.incoming = incoming;
    }

    public List<Outgoing> getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(List<Outgoing> outgoing) {
        this.outgoing = outgoing;
    }

    @Override
    public boolean hasInputVars() {
        return true;
    }

    @Override
    public boolean isSingleInputVar() {
        return false;
    }

    @Override
    public boolean hasOutputVars() {
        return true;
    }

    @Override
    public boolean isSingleOutputVar() {
        return false;
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         Objects.hashCode(incoming),
                                         Objects.hashCode(outgoing));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ConnectedBaseSubprocess) {
            ConnectedBaseSubprocess other = (ConnectedBaseSubprocess) o;
            return super.equals(other)
                    && Objects.equals(this.incoming, other.incoming)
                    && Objects.equals(this.outgoing, other.outgoing);
        }
        return false;
    }
}
