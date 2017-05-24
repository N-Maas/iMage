package org.iMage.geometrify;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * A polygon.
 *
 * @author Nikolai Maas
 * @version 1.0
 */
public class IPolygon implements IPrimitive {
	private final Polygon polygon;
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
	public IPolygon(Point... points) {
		if (points.length < 3) {
			throw new IllegalArgumentException("Illegal point number.");
		}
		this.polygon = new Polygon();
		for (Point p : points) {
			this.polygon.addPoint(p.x, p.y);
		}
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
		Rectangle rec = this.polygon.getBounds();
		int maxX = rec.x + rec.width;
		int maxY = rec.y + rec.height;
		ArrayList<Point> result = new ArrayList<>((maxX - rec.x) * (maxY - rec.y) / 2);

		for (int i = rec.x; i <= maxX; i++) {
			for (int j = rec.y; j <= maxY; j++) {
				Point p = new Point(i, j);
				if (this.polygon.contains(p)) {
					result.add(p);
				}
			}
		}
		result.trimToSize();
		this.insidePoints = result;
	}
}
