package gui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests value setting of LabeledSlider.
 * 
 * @author Nikolai
 */
public class LabeledSliderTest {

	/**
	 * Tests for too small values.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void minValueTest() {
		LabeledSlider ls = new LabeledSlider("", 20, 80, 40, 0, true);
		assertEquals(40, ls.getValue());
		ls.setValue(0);
	}

	/**
	 * Tests for too big values.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void maxValueTest() {
		LabeledSlider ls = new LabeledSlider("", 20, 200, 90, 0, true);
		assertEquals(90, ls.getValue());
		ls.setValue(220);
	}

	/**
	 * Tests value setting and getting.
	 */
	@Test
	public void valueTest() {
		LabeledSlider ls = new LabeledSlider("", 100, 10);
		assertEquals(10, ls.getValue());
		ls.setValue(50);
		assertEquals(50, ls.getValue());
	}

}
