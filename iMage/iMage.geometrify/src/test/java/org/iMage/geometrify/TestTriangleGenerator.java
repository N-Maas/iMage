package org.iMage.geometrify;

public class TestTriangleGenerator implements PrimitiveGenerator {
	private final IPointGenerator generator;

	public TestTriangleGenerator(IPointGenerator r) {
		this.generator = r;
	}

	@Override
	public Primitive generatePrimitive() {
		return new Triangle(this.generator.nextPoint(), this.generator.nextPoint(), this.generator.nextPoint());
	}
}
