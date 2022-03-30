package org.kie.workbench.common.stunner.bpmn.definition.models.drools;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;

public class Import {

    @XmlAttribute
    private String name;

    public Import() {
    }

    public Import(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Import)) {
            return false;
        }
        Import anImport = (Import) o;
        return Objects.equals(getName(), anImport.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
