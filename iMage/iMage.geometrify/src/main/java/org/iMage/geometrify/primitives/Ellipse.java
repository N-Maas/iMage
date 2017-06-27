package org.iMage.geometrify.primitives;

import java.awt.Point;

public class Ellipse extends AbstractPrimitive {
	public Ellipse(Point a, Point b) {
		super(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.max(a.x, b.x) - Math.min(a.x, b.x) + 1,
				Math.max(a.y, b.y) - Math.min(a.y, b.y) + 1);
	}

	@Override
	protected int[] calculatePoints() {
		int radX = this.getWidth() / 2;
		int radWidth = radX * radX;
		int radY = this.getHeight() / 2;
		int radHeight = radY * radY;
		double factor = ((double) radWidth) / radHeight;
		return super.buildPointsByOrigin((x, y) -> {
			int xVal = Math.abs(radX - x);
			int yVal = Math.abs(radY - y);
			return (xVal * xVal + factor * yVal * yVal) <= radWidth;
		});
	}
}
