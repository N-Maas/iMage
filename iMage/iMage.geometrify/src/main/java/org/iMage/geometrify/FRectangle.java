package org.iMage.geometrify;

import java.awt.Point;

/**
 * A polygon.
 *
 * @author Nikolai Maas
 * @version 1.0
 */
public class FRectangle extends AbstractPrimitive {

	/**
	 * Creates a new triangle from the given vertices. Negative coordinates or
	 * equal points are accepted, as they are not problematically.
	 *
	 * @param a
	 *            the first vertex
	 * @param b
	 *            the second vertex
	 * @param c
	 *            the third vertex
	 */
	public FRectangle(Point a, Point b) {
		super(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.max(a.x, b.x) - Math.min(a.x, b.x) + 1,
				Math.max(a.y, b.y) - Math.min(a.y, b.y) + 1);
	}

	@Override
	protected int[] calculatePoints() {
		return super.buildPoints((x, y) -> true);
	}
}
