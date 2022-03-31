package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlCData;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import org.kie.workbench.common.stunner.core.util.HashUtil;

@XmlRootElement(name = "Condition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class Condition {

    @XmlAttribute(namespace = "xsi")
    private String type = "bpmn2:tFormalExpression";

    @XmlAttribute
    private String language = "http://www.jboss.org/drools/rule";

    @XmlValue
    @XmlCData
    private String value;

    public Condition() {
    }

    public Condition(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Condition)) {
            return false;
        }
        Condition condition = (Condition) o;
        return Objects.equals(getType(), condition.getType())
                && Objects.equals(getLanguage(), condition.getLanguage())
                && Objects.equals(getValue(), condition.getValue());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(getValue()),
                                         Objects.hashCode(getLanguage()),
                                         Objects.hashCode(getType()));
    }
}
