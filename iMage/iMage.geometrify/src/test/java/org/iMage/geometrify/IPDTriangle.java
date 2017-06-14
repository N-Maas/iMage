package org.iMage.geometrify;

import java.awt.Point;

public class IPDTriangle extends Triangle {

	public IPDTriangle(Point a, Point b, Point c) {
		super(a, b, c);
	}

	private static long crossProduct(Point point, Point trianglePointA, Point trianglePointB) {
		int ax = point.x - trianglePointA.x;
		int ay = point.y - trianglePointA.y;
		int bx = trianglePointA.x - trianglePointB.x;
		int by = trianglePointA.y - trianglePointB.y;
		return ax * by - ay * bx;
	}

	@Override
	public boolean isInsidePrimitive(Point point) {
		boolean b1, b2, b3;

		b1 = crossProduct(point, a, b) > 0;
		b2 = crossProduct(point, b, c) > 0;
		b3 = crossProduct(point, c, a) > 0;
		return (b1 == b2) && (b2 == b3);
	}
}
