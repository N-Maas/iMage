package org.iMage.geometrify;

import java.awt.Point;

/**
 * A polygon.
 *
 * @author Nikolai Maas
 * @version 1.0
 */
public class IRectangle extends AbstractPrimitive {
	private final int xMin, xMax, yMin, yMax;

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
	public IRectangle(Point a, Point b) {
		this.xMin = Math.min(a.x, b.x);
		this.xMax = Math.max(a.x, b.x);
		this.yMin = Math.min(a.y, b.y);
		this.yMax = Math.max(a.y, b.y);
	}

	@Override
	protected int[] calculatePoints() {
		int width = this.xMax - this.xMin + 1;
		int[] result = new int[2 * width * (this.yMax - this.yMin + 1)];
		for (int i = this.xMin; i <= this.xMax; i++) {
			for (int j = this.yMin; j <= this.yMax; j++) {
				int index = (i - this.xMin) + (j - this.yMin) * width;
				result[index] = i;
				result[index + 1] = j;
			}
		}
		return result;
	}
}
