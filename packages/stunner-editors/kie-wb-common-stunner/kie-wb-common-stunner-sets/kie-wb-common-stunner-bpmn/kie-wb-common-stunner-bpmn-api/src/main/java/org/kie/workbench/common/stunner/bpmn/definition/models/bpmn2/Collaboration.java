package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.kie.workbench.common.stunner.core.util.HashUtil;

@XmlRootElement(name = "collaboration", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class Collaboration {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String name;

    private Participant participant;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Collaboration)) {
            return false;
        }

        Collaboration that = (Collaboration) o;

        return Objects.equals(getId(), that.getId())
                && Objects.equals(getName(), that.getName())
                && Objects.equals(getParticipant(), that.getParticipant());
    }

    @Override
    public int hashCode() {
        return HashUtil.combineHashCodes(Objects.hashCode(getId()),
                                         Objects.hashCode(getName()),
                                         Objects.hashCode(getParticipant()));
    }
}
