package org.iMage.geometrify;

public class TriangleGenerator implements PrimitiveGenerator {
	private final IPointGenerator generator;

	public TriangleGenerator(int width, int height) {
		this.generator = new RandomPointGenerator(width, height);
	}

	@Override
	public Primitive generatePrimitive() {
		return new Triangle(this.generator.nextPoint(), this.generator.nextPoint(), this.generator.nextPoint());
	}
}
