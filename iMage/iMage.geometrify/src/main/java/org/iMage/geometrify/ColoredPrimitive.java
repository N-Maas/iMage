package org.iMage.geometrify;

import java.awt.Color;

public class ColoredPrimitive implements IPrimitive {
	private final IPrimitive primitive;
	private final Color color;

	public ColoredPrimitive(IPrimitive primitive, Color color) {
		this.primitive = primitive;
		this.color = color;
	}

	@Override
	public int[] getInsidePoints() {
		return this.primitive.getInsidePoints();
	}

	public Color getColor() {
		return this.color;
	}
}
