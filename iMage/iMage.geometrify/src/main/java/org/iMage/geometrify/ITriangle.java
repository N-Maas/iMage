package org.iMage.geometrify;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A triangle.
 *
 * @author Dominic Ziegler, Martin Blersch
 * @version 1.0
 */
public class ITriangle extends AbstractPrimitive {
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
	public ITriangle(Point a, Point b, Point c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	private static long crossProduct(int x, int y, Point trianglePointA, Point trianglePointB) {
		int ax = x - trianglePointA.x;
		int ay = y - trianglePointA.y;
		int bx = trianglePointA.x - trianglePointB.x;
		int by = trianglePointA.y - trianglePointB.y;
		return ax * by - ay * bx;
	}

	public boolean isInsidePrimitive(int x, int y) {
		boolean b1, b2, b3;

		b1 = crossProduct(x, y, this.a, this.b) > 0;
		b2 = crossProduct(x, y, this.b, this.c) > 0;
		b3 = crossProduct(x, y, this.c, this.a) > 0;
		return (b1 == b2) && (b2 == b3);
	}

	@Override
	protected int[] calculatePoints() {
		int minX = Math.min(Math.min(this.a.x, this.b.x), this.c.x);
		int minY = Math.min(Math.min(this.a.y, this.b.y), this.c.y);
		int maxX = Math.max(Math.max(this.a.x, this.b.x), this.c.x);
		int maxY = Math.max(Math.max(this.a.y, this.b.y), this.c.y);
		List<Integer> pointList = new ArrayList<>();
		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				if (this.isInsidePrimitive(i, j)) {
					pointList.add(i);
					pointList.add(j);
				}
			}
		}
		return pointList.stream().mapToInt(i -> i).toArray();
	}
}
