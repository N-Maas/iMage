package org.iMage.geometrify;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A polygon.
 *
 * @author Nikolai Maas
 * @version 1.0
 */
public class IRectangle implements IPrimitive {
	private final int xMin, xMax, yMin, yMax;
	private List<Point> insidePoints;

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

	/**
	 * Creates a list of all points that are inside the specified primitive.
	 * 
	 * @param ip
	 *            the primitive
	 * @return list of all points inside the primitive
	 */
	@Override
	public List<Point> getInsidePoints() {
		if (this.insidePoints == null) {
			this.calculatePoints();
		}
		return this.insidePoints;
	}

	private void calculatePoints() {
		ArrayList<Point> result = new ArrayList<>((this.xMax - this.xMin + 1) * (this.yMax - this.yMin + 1) + 1);
		for (int i = this.xMin; i <= this.xMax; i++) {
			for (int j = this.yMin; j <= this.yMax; j++) {
				result.add(new Point(i, j));
			}
		}
		result.trimToSize();
		this.insidePoints = result;
	}
}
