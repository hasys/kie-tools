/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.stunner.bpmn.marshaller.functional.compatibility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.stunner.bpmn.marshaller.Marshaller;
import org.kie.workbench.common.stunner.core.diagram.Diagram;
import org.kie.workbench.common.stunner.core.diagram.Metadata;
import org.kie.workbench.common.stunner.core.graph.Graph;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.kie.workbench.common.stunner.bpmn.marshaller.Unmarshalling.unmarshall;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EclipseImportTest {

    Marshaller marshaller = new Marshaller();

    private static final String BPMN_FILE_PATH = "org/kie/workbench/common/stunner/bpmn/backend/service/diagram/SimpleProcess.bpmn";

    @Test
    public void testUnmarshallSimpleProcess() throws Exception {
        Diagram<Graph, Metadata> diagram = unmarshall(marshaller, BPMN_FILE_PATH);
        assertNotNull(diagram);
    }
}
