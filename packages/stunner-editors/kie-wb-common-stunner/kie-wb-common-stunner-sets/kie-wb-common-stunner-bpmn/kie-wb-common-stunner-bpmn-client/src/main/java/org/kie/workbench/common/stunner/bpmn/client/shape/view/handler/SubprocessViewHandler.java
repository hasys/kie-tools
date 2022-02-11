/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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

package org.kie.workbench.common.stunner.bpmn.client.shape.view.handler;

import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.BaseSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.MultipleInstanceSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2.ReusableSubprocess;
import org.kie.workbench.common.stunner.bpmn.definition.property.task.ReusableSubprocessTaskExecutionSet;
import org.kie.workbench.common.stunner.core.client.shape.view.ShapeViewHandler;
import org.kie.workbench.common.stunner.svg.client.shape.view.SVGShapeView;
import org.kie.workbench.common.stunner.svg.client.shape.view.SVGViewUtils;

public class SubprocessViewHandler implements ShapeViewHandler<BaseSubprocess, SVGShapeView<?>> {

    static final String REUSABLE_SUB_PROCESS_NORMAL_BOUNDING_BOX = "subProcessReusableNormalBoundingBox";
    static final String REUSABLE_SUB_PROCESS_NORMAL_REUSABLE_ICON = "subProcessReusableNormalReusableIcon";
    static final String REUSABLE_SUB_PROCESS_MI_BOUNDING_BOX = "subProcessReusableMIBoundingBox";
    static final String REUSABLE_SUB_PROCESS_MI_REUSABLE_ICON = "subProcessReusableMIReusableIcon";
    static final String REUSABLE_SUB_PROCESS_MI_MULTIPLE_INSTANCE_ICON_PARALLEL = "subProcessReusableMIIMultipleInstanceIconParallel";
    static final String REUSABLE_SUB_PROCESS_MI_MULTIPLE_INSTANCE_ICON_SEQUENTIAL = "subProcessReusableMIIMultipleInstanceIconSequential";

    static final String MULTIPLE_INSTANCE_SUB_PROCESS_ICON_PARALLEL = "multipleInstanceIconParallel";
    static final String MULTIPLE_INSTANCE_SUB_PROCESS_ICON_SEQUENTIAL = "multipleInstanceIconSequential";

    @Override
    public void handle(BaseSubprocess subprocess, SVGShapeView<?> view) {
        if (subprocess instanceof ReusableSubprocess) {
            final ReusableSubprocess reusableSubprocess = (ReusableSubprocess) subprocess;
            ReusableSubprocessTaskExecutionSet executionSet = (ReusableSubprocessTaskExecutionSet) reusableSubprocess.getExecutionSet();
            final boolean multipleInstance = executionSet.getIsMultipleInstance().getValue();
            final boolean sequential = executionSet.getMultipleInstanceExecutionMode().isSequential();
            final double normalFillAlpha = multipleInstance ? 0 : 1;
            final double normalFillStroke = multipleInstance ? 0 : 1;
            final double miFillAlpha = multipleInstance ? 1 : 0;
            final double miFillStroke = multipleInstance ? 1 : 0;

            SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_NORMAL_BOUNDING_BOX, normalFillAlpha, normalFillStroke);
            SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_NORMAL_REUSABLE_ICON, normalFillAlpha, normalFillStroke);

            SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_MI_BOUNDING_BOX, miFillAlpha, miFillStroke);
            SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_MI_REUSABLE_ICON, miFillAlpha, miFillStroke);

            SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_MI_MULTIPLE_INSTANCE_ICON_PARALLEL, 0, 0);
            SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_MI_MULTIPLE_INSTANCE_ICON_SEQUENTIAL, 0, 0);

            if (multipleInstance) {
                if (sequential) {
                    SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_MI_MULTIPLE_INSTANCE_ICON_SEQUENTIAL, 1, 1);
                } else {
                    SVGViewUtils.setFillAndStroke(view, REUSABLE_SUB_PROCESS_MI_MULTIPLE_INSTANCE_ICON_PARALLEL, 1, 1);
                }
            }
        } else if (subprocess instanceof MultipleInstanceSubprocess) {
            final boolean sequential = ((MultipleInstanceSubprocess) subprocess).getExecutionSet().getMultipleInstanceExecutionMode().isSequential();
            if (sequential) {
                SVGViewUtils.setFillAndStroke(view, MULTIPLE_INSTANCE_SUB_PROCESS_ICON_SEQUENTIAL, 1, 1);
                SVGViewUtils.setFillAndStroke(view, MULTIPLE_INSTANCE_SUB_PROCESS_ICON_PARALLEL, 0, 0);
            } else {
                SVGViewUtils.setFillAndStroke(view, MULTIPLE_INSTANCE_SUB_PROCESS_ICON_PARALLEL, 1, 1);
                SVGViewUtils.setFillAndStroke(view, MULTIPLE_INSTANCE_SUB_PROCESS_ICON_SEQUENTIAL, 0, 0);
            }
        }
    }
}
