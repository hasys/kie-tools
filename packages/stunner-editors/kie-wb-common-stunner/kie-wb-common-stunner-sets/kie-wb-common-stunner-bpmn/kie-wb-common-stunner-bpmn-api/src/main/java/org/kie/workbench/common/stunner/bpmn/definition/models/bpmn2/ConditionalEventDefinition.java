package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

import org.kie.workbench.common.stunner.core.util.HashUtil;

@XmlRootElement(name = "conditionalEventDefinition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class ConditionalEventDefinition extends AbstractEventDefinition {

    private Condition condition;

    public ConditionalEventDefinition() {
    }

    public ConditionalEventDefinition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConditionalEventDefinition)) {
            return false;
        }
        ConditionalEventDefinition that = (ConditionalEventDefinition) o;
        return Objects.equals(getCondition(), that.getCondition());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(getCondition()));
    }
}
