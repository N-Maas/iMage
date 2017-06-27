package org.iMage.geometrify.primitives;

import java.awt.Color;

public interface Primitive {

	int[] getInsidePoints();

	boolean isInsidePrimitive(int x, int y);

	boolean isInsideBounds(int x, int y);

	int getMinX();

	int getMinY();

	int getWidth();

	int getHeight();

	default ColoredPrimitive ofColor(Color c) {
		return new ColoredPrimitive(this, c);
	}

	default ColoredPrimitive ofColor(int i) {
		return new ColoredPrimitive(this, i);
	}

	static Primitive emptyPrimitive(int x, int y) {
		return new Primitive() {

			@Override
			public boolean isInsidePrimitive(int x, int y) {
				return false;
			}

			@Override
			public boolean isInsideBounds(int x, int y) {
				return false;
			}

			@Override
			public int getWidth() {
				return 0;
			}

			@Override
			public int getMinX() {
				return x;
			}

			@Override
			public int getMinY() {
				return y;
			}

			@Override
			public int[] getInsidePoints() {
				return new int[0];
			}

			@Override
			public int getHeight() {
				return 0;
			}
		};
	}
}
