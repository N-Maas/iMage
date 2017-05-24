package org.iMage.geometrify;

import java.awt.Color;
import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the different methods of the Triangle class.
 * 
 * @author Nikolai
 */
public class TriangleTest {

	/**
	 * Tests whether the constructor works.
	 */
	@Test
	public void custructorTest() {
		new Triangle(new Point(0, 0), new Point(1, 0), new Point(0, 1));
		new Triangle(new Point(2, 2), new Point(50, 50), new Point(120, 120));
	}

	/**
	 * Tests the correctness of the isInsidePrimitive() method by applying it to
	 * different points.
	 */
	@Test
	public void insideTest() {
		Triangle tri = new Triangle(new Point(0, 0), new Point(2, 0), new Point(0, 3));
		Assert.assertTrue(tri.isInsidePrimitive(new Point(1, 0)));
		Assert.assertTrue(tri.isInsidePrimitive(new Point(0, 1)));
		Assert.assertTrue(tri.isInsidePrimitive(new Point(1, 1)));
		Assert.assertFalse(tri.isInsidePrimitive(new Point(2, 1)));
		Assert.assertFalse(tri.isInsidePrimitive(new Point(1, 2)));
		Assert.assertFalse(tri.isInsidePrimitive(new Point(-1, 1)));
		tri = new Triangle(new Point(0, 0), new Point(0, 0), new Point(0, 0));
		Assert.assertFalse(tri.isInsidePrimitive(new Point(0, 0)));
	}

	/**
	 * Tests whether the corner points of a bounding box are calculated
	 * correctly.
	 */
	@Test
	public void boundigBoxTest() {
		Triangle tri = new Triangle(new Point(2, 3), new Point(4, 2), new Point(3, 10));
		BoundingBox bb = tri.getBoundingBox();
		Assert.assertEquals(new Point(2, 2), bb.getUpperLeftCorner());
		Assert.assertEquals(new Point(4, 10), bb.getLowerRightCorner());
	}

	/**
	 * Tests whether getColor() throws an exception, if no color is set.
	 */
	@Test(expected = IllegalStateException.class)
	public void colorExceptionTest() {
		Triangle tri = new Triangle(new Point(0, 0), new Point(1, 0), new Point(0, 1));
		tri.getColor();
	}

	/**
	 * Tests setting and getting of colors.
	 */
	@Test
	public void colorTest() {
		Triangle tri = new Triangle(new Point(0, 0), new Point(1, 0), new Point(0, 1));
		tri.setColor(Color.BLACK);
		Assert.assertEquals(Color.BLACK, tri.getColor());
		tri.setColor(Color.RED);
		Assert.assertEquals(Color.RED, tri.getColor());
	}

}
