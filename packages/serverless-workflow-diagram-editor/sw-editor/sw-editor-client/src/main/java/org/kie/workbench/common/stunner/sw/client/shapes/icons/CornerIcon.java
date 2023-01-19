/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
package org.kie.workbench.common.stunner.sw.client.shapes.icons;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.toolbox.items.tooltip.PrimitiveTextTooltip;
import com.ait.lienzo.shared.core.types.EventPropagationMode;
import elemental2.dom.DomGlobal;

public class CornerIcon extends Group {

    private final Rectangle border = new Rectangle(20, 20)
            .setFillColor("white")
            .setFillAlpha(0.001)
            .setStrokeAlpha(0.001)
            .setStrokeColor("white")
            .setCornerRadius(9)
            .setEventPropagationMode(EventPropagationMode.NO_ANCESTORS)
            .setListening(true);

    private String tooltipText;

    public CornerIcon(String icon, String tooltip) {
        setX(224);
        setY(6);
        setListening(true);
        add(border);
        this.tooltipText = tooltip;

        PrimitiveTextTooltip tooltipElement = PrimitiveTextTooltip.Builder.build(tooltipText);
        tooltipElement.withText(t -> {
            moveToTop();
            t.setText(tooltipText);
            t.setFontSize(12);
        });
        add(tooltipElement.asPrimitive());
        tooltipElement.forComputedBoundingBox(border::getBoundingBox);

        MultiPath clockIcon = new MultiPath(icon)
                .setScale(2)
                .setStrokeWidth(0)
                .setFillColor("#CCC")
                .setListening(false);
        add(clockIcon);

        border.addNodeMouseEnterHandler(event -> {
            tooltipElement.show();
            clockIcon.setFillColor("#4F5255");
        });
        border.addNodeMouseExitHandler(event -> {
            tooltipElement.hide();
            clockIcon.setFillColor("#CCC");
            border.getLayer().batch();
        });
        border.addNodeMouseClickHandler(event -> {
            DomGlobal.console.log("clicked!");
        });
    }

    public void setTooltipText(String text) {
        tooltipText = text;
    }
}
