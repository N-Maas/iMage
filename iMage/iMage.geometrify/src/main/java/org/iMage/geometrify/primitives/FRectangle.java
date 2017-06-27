package org.iMage.geometrify.primitives;

import java.awt.Point;

/**
 * A rectangle.
 *
 * @author Nikolai Maas
 * @version 1.0
 */
public class FRectangle extends AbstractPrimitive {

	public FRectangle(Point a, Point b) {
		super(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.max(a.x, b.x) - Math.min(a.x, b.x) + 1,
				Math.max(a.y, b.y) - Math.min(a.y, b.y) + 1);
	}

	@Override
	protected int[] calculatePoints() {
		return super.buildPoints((x, y) -> true);
	}
}
