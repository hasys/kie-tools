package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "import", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class Import {

    @XmlAttribute
    private String importType = "http://schemas.xmlsoap.org/wsdl/";

    @XmlAttribute
    private String location;

    @XmlAttribute
    private String namespace;

    public Import() {
    }

    public Import(String location, String namespace) {
        this.location = location;
        this.namespace = namespace;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
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
        return Objects.equals(getImportType(), anImport.getImportType())
                && Objects.equals(getLocation(), anImport.getLocation())
                && Objects.equals(getNamespace(), anImport.getNamespace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImportType(),
                            getLocation(),
                            getNamespace());
    }
}
