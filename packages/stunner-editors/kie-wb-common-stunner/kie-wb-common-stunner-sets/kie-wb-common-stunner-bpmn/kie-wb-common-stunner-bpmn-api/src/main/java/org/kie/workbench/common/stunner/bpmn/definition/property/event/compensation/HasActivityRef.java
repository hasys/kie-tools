package org.kie.workbench.common.stunner.bpmn.definition.property.event.compensation;

public interface HasActivityRef {

    void updateActivityRef(String newActivityRef);

    String getActivityRef();
}
