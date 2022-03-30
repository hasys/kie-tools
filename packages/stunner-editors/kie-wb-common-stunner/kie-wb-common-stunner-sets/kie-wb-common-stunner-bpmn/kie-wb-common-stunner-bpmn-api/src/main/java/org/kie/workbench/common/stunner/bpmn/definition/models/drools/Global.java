package org.kie.workbench.common.stunner.bpmn.definition.models.drools;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;

public class Global {

    @XmlAttribute
    private String identifier;

    @XmlAttribute
    private String type;

    public Global() {
    }

    public Global(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Global)) {
            return false;
        }
        Global global = (Global) o;
        return Objects.equals(getIdentifier(), global.getIdentifier())
                && Objects.equals(getType(), global.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(),
                            getType());
    }
}
