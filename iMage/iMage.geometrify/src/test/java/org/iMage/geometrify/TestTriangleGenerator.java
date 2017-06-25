package org.iMage.geometrify;

public class TestTriangleGenerator implements IPrimitiveGenerator {
	private final IPointGenerator generator;

	public TestTriangleGenerator(IPointGenerator r) {
		this.generator = r;
	}

	@Override
	public IPrimitive generatePrimitive() {
		return new ITriangle(this.generator.nextPoint(), this.generator.nextPoint(), this.generator.nextPoint());
	}
}
