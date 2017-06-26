package org.iMage.geometrify;

import java.awt.Color;

public interface Primitive {
	int[] getInsidePoints();

	boolean isInsidePrimitive(int x, int y);

	boolean isInsideBounds(int x, int y);

	int getMinX();

	int getMinY();

	int getWidth();

	int getHeight();

	default ColoredPrimitive colored(Color c) {
		return new ColoredPrimitive(this, c);
	}

	default ColoredPrimitive colored(int i) {
		return new ColoredPrimitive(this, i);
	}
}
