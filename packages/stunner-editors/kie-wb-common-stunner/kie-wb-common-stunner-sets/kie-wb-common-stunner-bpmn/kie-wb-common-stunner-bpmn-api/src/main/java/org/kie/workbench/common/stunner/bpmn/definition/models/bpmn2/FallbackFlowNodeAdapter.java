package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

public class FallbackFlowNodeAdapter {

    public static void unmarshal(FlowNode to, FlowNode from) {
        if (to == null || from == null) {
            return;
        }

        to.setId(from.getId());
        to.setName(from.getName());
        to.setDocumentation(from.getDocumentation());

        if (from.getAdvancedData() == null || from.getAdvancedData() == null) {
            return;
        }

        to.getAdvancedData().setMetaDataAttributes(from.getAdvancedData().getMetaDataAttributes());
    }
}
