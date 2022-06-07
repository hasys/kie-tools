package org.kie.workbench.common.stunner.bpmn.definition.property.assignment;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataInputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.DataOutputAssociation;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.From;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.SourceRef;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.TargetRef;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.To;

import static org.assertj.core.api.Assertions.assertThat;

public class AssignmentParserTest {

    private final String NODE_ID = "TEST_ID";

    private final String INPUT_VARS = "|varInTask:custom.data.Type:,varInConstantInt:Integer:|||[din]varIn->varInTask,[din]varInConstantInt=123";

    private final String OUTPUT_VARS = "|||varOutTask:String:,varOutExpression:String:|[dout]varOutTask->varOut,[dout]%23%7Bexpression.out%7D=varOutExpression";

    @Test
    public void testParseDataInputAssociation() {
        List<DataInputAssociation> associations = new ArrayList<>();
        DataInputAssociation association1 = new DataInputAssociation(generateInputIdRef("varInTask"), "varIn");
        DataInputAssociation association2 = new DataInputAssociation();
        association2.setTargetRef(new TargetRef(generateInputIdRef("varInConstantInt")));
        association2.getAssignment().setFrom(new From("123"));
        association2.getAssignment().setTo(new To(association2.getTargetRef().getValue()));

        associations.add(association1);
        associations.add(association2);
        assertThat(associations).hasSameElementsAs(AssignmentParser.parseDataInputAssociation(NODE_ID, INPUT_VARS));
    }

    @Test
    public void testParseDataOutputAssociation() {
        List<DataOutputAssociation> associations = new ArrayList<>();
        DataOutputAssociation association1 = new DataOutputAssociation(generateOutputIdRef("varOutTask"), "varOut");
        DataOutputAssociation association2 = new DataOutputAssociation();
        association2.setSourceRef(new SourceRef(generateOutputIdRef("varOutExpression")));
        association2.getAssignment().setFrom(new From(association2.getSourceRef().getValue()));
        association2.getAssignment().setTo(new To("%23%7Bexpression.out%7D"));

        associations.add(association1);
        associations.add(association2);
        assertThat(associations).hasSameElementsAs(AssignmentParser.parseDataOutputAssociation(NODE_ID, OUTPUT_VARS));
    }

    private String generateInputIdRef(String name) {
        return generateIdRef(name, "InputX");
    }

    private String generateOutputIdRef(String name) {
        return generateIdRef(name, "OutputX");
    }

    private String generateIdRef(String name, String type) {
        return NODE_ID + "_" + name + type;
    }
}