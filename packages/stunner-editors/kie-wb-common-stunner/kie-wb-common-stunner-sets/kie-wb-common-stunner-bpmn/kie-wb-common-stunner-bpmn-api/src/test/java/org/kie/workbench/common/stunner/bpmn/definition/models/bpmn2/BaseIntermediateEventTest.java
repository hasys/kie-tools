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

package org.kie.workbench.common.stunner.bpmn.definition.models.bpmn2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.workbench.common.stunner.bpmn.definition.property.background.BackgroundSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dataio.DataIOSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.dimensions.CircleDimensionSet;
import org.kie.workbench.common.stunner.bpmn.definition.property.font.FontSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class BaseIntermediateEventTest {

    private static final String FAKE_LABEL = "fake_label";
    private static final String ANOTHER_LABEL = "another_label";

    private BackgroundSet backgroundSet;
    private FontSet fontSet;
    private CircleDimensionSet dimensionsSet;
    private DataIOSet dataIOSet;

    private FakeBaseIntermediateEventTest tested;

    @Before
    public void setUp() {
        backgroundSet = mock(BackgroundSet.class);
        fontSet = mock(FontSet.class);
        dimensionsSet = mock(CircleDimensionSet.class);
        dataIOSet = mock(DataIOSet.class);
        tested = spy(new FakeBaseIntermediateEventTest());
    }

    @Test
    public void initLabels() {
        tested.initLabels();
        assertTrue(tested.labels.contains(FAKE_LABEL));
    }

    @Test
    public void getBackgroundSet() {
        tested.backgroundSet = backgroundSet;
        Assert.assertEquals(backgroundSet, tested.getBackgroundSet());
    }

    @Test
    public void setBackgroundSet() {
        tested.backgroundSet = null;
        tested.setBackgroundSet(backgroundSet);
        Assert.assertEquals(backgroundSet, tested.backgroundSet);
    }

    @Test
    public void getFontSet() {
        tested.fontSet = fontSet;
        Assert.assertEquals(fontSet, tested.getFontSet());
    }

    @Test
    public void setFontSet() {
        tested.fontSet = null;
        tested.setFontSet(fontSet);
        Assert.assertEquals(fontSet, tested.fontSet);
    }

    @Test
    public void getDimensionsSet() {
        tested.dimensionsSet = dimensionsSet;
        Assert.assertEquals(dimensionsSet, tested.getDimensionsSet());
    }

    @Test
    public void setDimensionsSet() {
        tested.dimensionsSet = null;
        tested.setDimensionsSet(dimensionsSet);
        Assert.assertEquals(dimensionsSet, tested.dimensionsSet);
    }

    @Test
    public void getDataIOSet() {
        tested.dataIOSet = dataIOSet;
        Assert.assertEquals(dataIOSet, tested.getDataIOSet());
    }

    @Test
    public void setDataIOSet() {
        tested.dataIOSet = null;
        tested.setDataIOSet(dataIOSet);
        Assert.assertEquals(dataIOSet, tested.dataIOSet);
    }

    @Test
    public void getLabels() {
        Assert.assertEquals(tested.labels, tested.getLabels());
    }

    @Test
    public void testEquals() {
        tested.backgroundSet = mock(BackgroundSet.class);
        tested.fontSet = mock(FontSet.class);
        tested.dimensionsSet = mock(CircleDimensionSet.class);
        tested.labels.clear();
        tested.labels.add(FAKE_LABEL);

        IntermediateTimerEvent compare1 = new IntermediateTimerEvent();

        FakeBaseIntermediateEventTest compare2 = new FakeBaseIntermediateEventTest();
        compare2.backgroundSet = mock(BackgroundSet.class);
        compare2.fontSet = mock(FontSet.class);
        compare2.dimensionsSet = mock(CircleDimensionSet.class);
        compare2.labels.add(ANOTHER_LABEL);

        FakeBaseIntermediateEventTest compare3 = new FakeBaseIntermediateEventTest();
        compare3.backgroundSet = backgroundSet;
        compare3.fontSet = fontSet;
        compare3.dimensionsSet = dimensionsSet;
        compare3.labels.add(FAKE_LABEL);

        assertFalse(tested.equals(compare1));
        assertFalse(tested.equals(compare2));
        assertFalse(tested.equals(compare3));
        assertTrue(tested.equals(tested));
    }

    private class FakeBaseIntermediateEventTest extends BaseIntermediateEvent {

        @Override
        protected void initLabels() {
            labels.add(FAKE_LABEL);
        }
    }
}