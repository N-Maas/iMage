package org.iMage.geometrify;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * A triangle.
 *
 * @author originally by Dominic Ziegler, Martin Blersch
 * @version 1.0
 */
public class Triangle extends AbstractPrimitive {
	// Alternative Implementierung:
	// private Polygon polygon;
	protected Point a, b, c;

	/**
	 * Creates a new triangle from the given vertices.
	 *
	 * @param a
	 *            the first vertex
	 * @param b
	 *            the second vertex
	 * @param c
	 *            the third vertex
	 */
	public Triangle(Point a, Point b, Point c) {
		super(calculateBounds(a, b, c));
		this.a = a;
		this.b = b;
		this.c = c;
	}

	private static Rectangle calculateBounds(Point a, Point b, Point c) {
		int minX = Math.min(Math.min(a.x, b.x), c.x);
		int maxX = Math.max(Math.max(a.x, b.x), c.x);
		int minY = Math.min(Math.min(a.y, b.y), c.y);
		int maxY = Math.max(Math.max(a.y, b.y), c.y);
		return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
	}

	private static long crossProduct(int x, int y, Point trianglePointA, Point trianglePointB) {
		int ax = x - trianglePointA.x;
		int ay = y - trianglePointA.y;
		int bx = trianglePointA.x - trianglePointB.x;
		int by = trianglePointA.y - trianglePointB.y;
		return ax * by - ay * bx;
	}

	@Override
	protected int[] calculatePoints() {
		List<Integer> pointList = new ArrayList<>();
		for (int i = this.getMinX(); i < this.getMinX() + this.getWidth(); i++) {
			for (int j = this.getMinY(); j < this.getMinY() + this.getHeight(); j++) {
				if (this.calculateInsidePrimitive(i, j)) {
					pointList.add(i);
					pointList.add(j);
				}
			}
		}
		return pointList.stream().mapToInt(i -> i).toArray();
	}

	private boolean calculateInsidePrimitive(int x, int y) {
		boolean b1, b2, b3;

		b1 = crossProduct(x, y, this.a, this.b) > 0;
		b2 = crossProduct(x, y, this.b, this.c) > 0;
		b3 = crossProduct(x, y, this.c, this.a) > 0;
		return (b1 == b2) && (b2 == b3);
	}
}
