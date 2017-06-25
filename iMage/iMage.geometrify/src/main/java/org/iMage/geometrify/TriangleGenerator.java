package org.iMage.geometrify;

public class TriangleGenerator implements IPrimitiveGenerator {
	private final IPointGenerator generator;

	public TriangleGenerator(int width, int height) {
		this.generator = new RandomPointGenerator(width, height);
	}

	@Override
	public IPrimitive generatePrimitive() {
		return new ITriangle(this.generator.nextPoint(), this.generator.nextPoint(), this.generator.nextPoint());
	}
}
