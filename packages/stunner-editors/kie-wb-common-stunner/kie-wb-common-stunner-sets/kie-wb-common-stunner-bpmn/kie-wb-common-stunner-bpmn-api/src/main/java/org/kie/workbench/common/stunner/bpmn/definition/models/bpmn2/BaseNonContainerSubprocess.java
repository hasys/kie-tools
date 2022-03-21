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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.kie.workbench.common.stunner.bpmn.definition.BPMNCategories;
import org.kie.workbench.common.stunner.bpmn.definition.HasIncoming;
import org.kie.workbench.common.stunner.bpmn.definition.HasOutgoing;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.RectangleDimensionsSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.simulation.SimulationSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.AdvancedData;
import org.kie.workbench.common.stunner.bpmn.definition.property.variables.ProcessData;
import org.kie.workbench.common.stunner.core.definition.annotation.definition.Category;
import org.kie.workbench.common.stunner.core.definition.annotation.morph.MorphBase;
import org.kie.workbench.common.stunner.core.util.HashUtil;
import org.treblereel.gwt.xml.mapper.api.annotation.XmlUnwrappedCollection;

@MorphBase(defaultType = ReusableSubprocess.class, targets = {BaseTask.class})
public abstract class BaseNonContainerSubprocess extends BaseSubprocess implements HasOutgoing,
                                                                                   HasIncoming {

    @Category
    public static final transient String category = BPMNCategories.SUB_PROCESSES;

    @XmlUnwrappedCollection
    private List<Incoming> incoming = new ArrayList<>();

    @XmlUnwrappedCollection
    private List<Outgoing> outgoing = new ArrayList<>();

    public BaseNonContainerSubprocess(String name,
                                      String documentation,
                                      BackgroundSet backgroundSet,
                                      FontSet fontSet,
                                      RectangleDimensionsSet dimensionsSet,
                                      SimulationSet simulationSet,
                                      AdvancedData advancedData) {
        super(name, documentation, backgroundSet, fontSet, dimensionsSet, simulationSet, advancedData, new ProcessData());
    }

    public abstract DataIOSet getDataIOSet();

    public abstract void setDataIOSet(DataIOSet dataIOSet);

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
    public int hashCode() {
        return HashUtil.combineHashCodes(super.hashCode(),
                                         Objects.hashCode(incoming),
                                         Objects.hashCode(outgoing));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseNonContainerSubprocess) {
            BaseNonContainerSubprocess other = (BaseNonContainerSubprocess) o;
            return super.equals(other)
                    && Objects.equals(this.incoming, other.incoming)
                    && Objects.equals(this.outgoing, other.outgoing);
        }
        return false;
    }
}
