package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

public class FallbackFlowNodeAdapter {

    public static void unmarshal(FlowNode to, FlowNode from) {
        to.setId(from.getId());
        to.setName(from.getName());
        to.setDocumentation(from.getDocumentation());
        to.getAdvancedData().setMetaDataAttributes(from.getAdvancedData().getMetaDataAttributes());
    }
}
