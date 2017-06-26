package org.iMage.geometrify;

import java.awt.Color;

public class ColoredPrimitive implements ColorPrimitive {
	private final Primitive primitive;
	private final Color color;

	public ColoredPrimitive(Primitive primitive, int rgb) {
		this(primitive, new Color(rgb, true));
	}

	public ColoredPrimitive(Primitive primitive, Color color) {
		this.primitive = primitive;
		this.color = color;
	}

	@Override
	public boolean isInsidePrimitive(int x, int y) {
		return this.primitive.isInsidePrimitive(x, y);
	}

	@Override
	public boolean isInsideBounds(int x, int y) {
		return this.primitive.isInsideBounds(x, y);
	}

	@Override
	public int getMinX() {
		return this.primitive.getMinX();
	}

	@Override
	public int getMinY() {
		return this.primitive.getMinY();
	}

	@Override
	public int getWidth() {
		return this.primitive.getWidth();
	}

	@Override
	public int getHeight() {
		return this.primitive.getHeight();
	}

	@Override
	public int[] getInsidePoints() {
		return this.primitive.getInsidePoints();
	}

	@Override
	public Color getColor() {
		return this.color;
	}
}
