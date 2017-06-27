package org.iMage.geometrify.generators;

import org.iMage.geometrify.primitives.Primitive;

public interface PrimitiveGenerator {
	Primitive generatePrimitive();

	int getWidth();

	int getHeight();

	int getMinX();

	int getMinY();
}
