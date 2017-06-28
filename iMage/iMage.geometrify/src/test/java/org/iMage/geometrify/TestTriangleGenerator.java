package org.iMage.geometrify;

import org.iMage.geometrify.generators.PrimitiveGenerator;
import org.iMage.geometrify.primitives.Primitive;
import org.iMage.geometrify.primitives.Triangle;

public class TestTriangleGenerator implements PrimitiveGenerator {
	private final IPointGenerator generator;

	public TestTriangleGenerator(IPointGenerator r) {
		this.generator = r;
	}

	@Override
	public Primitive generatePrimitive() {
		return new Triangle(this.generator.nextPoint(), this.generator.nextPoint(), this.generator.nextPoint());
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinY() {
		// TODO Auto-generated method stub
		return 0;
	}
}
