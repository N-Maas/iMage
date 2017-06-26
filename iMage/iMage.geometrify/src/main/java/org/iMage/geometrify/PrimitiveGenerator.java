package org.iMage.geometrify;

public interface PrimitiveGenerator {
	Primitive generatePrimitive();

	int getWidth();

	int getHeight();

	int getMinX();

	int getMinY();
}
