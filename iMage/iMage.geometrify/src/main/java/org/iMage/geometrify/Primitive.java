package org.iMage.geometrify;

public interface Primitive {
	int[] getInsidePoints();

	boolean isInsidePrimitive(int x, int y);

	boolean isInsideBounds(int x, int y);

	int getMinX();

	int getMinY();

	int getWidth();

	int getHeight();
}
