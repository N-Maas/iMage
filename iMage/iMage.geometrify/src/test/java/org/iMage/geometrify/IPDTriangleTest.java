package org.iMage.geometrify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Point;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for {@link Triangle}.
 *
 * @author Dominic Ziegler, Tobias Hey
 * @version 1.0
 */
public class IPDTriangleTest {
	private IPrimitive primitive, miniPrimitive;

	@Before
	public void setUp() {
		Point a = new Point(1, 1);
		Point b = new Point(10, 1);
		Point c = new Point(1, 10);
		primitive = new Triangle(a, b, c);
		Point a1 = new Point(0, 0);
		Point b1 = new Point(0, 1);
		Point c1 = new Point(1, 0);
		miniPrimitive = new Triangle(a1, b1, c1);
	}

	/**
	 * Test method for {@link Triangle#isInsidePrimitive(Point)}.
	 */
	@Test
	public void testIsInsidePrimitive() {
		assertTrue("(5|5) must be inside", primitive.isInsidePrimitive(new Point(5, 5)));
		assertTrue("(3|2) must be inside", primitive.isInsidePrimitive(new Point(3, 2)));
		assertTrue("(2|3) must be inside", primitive.isInsidePrimitive(new Point(2, 3)));
		assertFalse("(6|6) must be outside", primitive.isInsidePrimitive(new Point(6, 6)));
		assertFalse("(0|2) must be outside", primitive.isInsidePrimitive(new Point(0, 2)));
		assertFalse("(2|0) must be outside", primitive.isInsidePrimitive(new Point(2, 0)));
		assertFalse("(9|0) must be outside", primitive.isInsidePrimitive(new Point(9, 0)));
		assertFalse("(0|9) must be outside", primitive.isInsidePrimitive(new Point(0, 9)));
		assertFalse("(8|4) must be outside", primitive.isInsidePrimitive(new Point(8, 4)));
		assertFalse("(3|9) must be outside", primitive.isInsidePrimitive(new Point(3, 9)));
	}

	@Test
	public void testIsInsideMini() {
		assertTrue("(0|0) must be outside", miniPrimitive.isInsidePrimitive(new Point(0, 0)));
		assertTrue("(0|1) must be outside", miniPrimitive.isInsidePrimitive(new Point(0, 1)));
		assertTrue("(1|0) must be outside", miniPrimitive.isInsidePrimitive(new Point(1, 0)));
	}

	/**
	 * Tests if the {@link Point}s on the edges are inside the {@link Triangle}.
	 *
	 * If this failes, you should check if the reason is a different assumption
	 * on what should be inside or a failure in the implementation. Former is ok
	 * and just a conceptual difference
	 */
	@Test
	@Ignore
	public void testEdgesAreInsidePrimitive() {
		assertTrue("(5|1) should be inside", primitive.isInsidePrimitive(new Point(5, 1)));
		assertTrue("(1|5) should be inside", primitive.isInsidePrimitive(new Point(1, 5)));
		assertTrue("(9|2) should be inside", primitive.isInsidePrimitive(new Point(9, 2)));
		assertTrue("(2|9) should be inside", primitive.isInsidePrimitive(new Point(2, 9)));
		assertTrue("(6|5) should be inside", primitive.isInsidePrimitive(new Point(6, 5)));
	}

	/**
	 * Tests if the {@link Point}s on the corners are inside the
	 * {@link Triangle}.
	 *
	 * If this failes, you should check if the reason is a different assumption
	 * on what should be inside or a failure in the implementation. Former is ok
	 * and just a conceptual difference
	 */
	@Test
	@Ignore
	public void testPointsAreInsidePrimitive() {
		assertTrue("(1|1) should be inside", primitive.isInsidePrimitive(new Point(1, 1)));
		assertTrue("(1|10) should be inside", primitive.isInsidePrimitive(new Point(1, 10)));
		assertTrue("(10|1) should be inside", primitive.isInsidePrimitive(new Point(10, 1)));
	}

	/**
	 * Test method for {@link Triangle#getBoundingBox()}.
	 */
	@Test
	public void testGetBoundingBox() {
		BoundingBox bbox = primitive.getBoundingBox();
		assertEquals(new Point(1, 1), bbox.getUpperLeftCorner());
		assertEquals(new Point(10, 10), bbox.getLowerRightCorner());
	}

	/**
	 * Test case for {@link Triangle#setColor()} and
	 * {@link Triangle#getColor()}.
	 */
	@Test
	public void testColor() {
		primitive.setColor(Color.PINK);
		assertEquals(Color.PINK, primitive.getColor());
	}
}
