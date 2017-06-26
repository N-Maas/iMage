package org.iMage.geometrify;

public class RectangleGenerator implements IPrimitiveGenerator {
	private final RandomPointGenerator generator;

	public RectangleGenerator(int width, int height) {
		this.generator = new RandomPointGenerator(width, height);
	}

	@Override
	public Primitive generatePrimitive() {
		return new FRectangle(this.generator.nextPoint(), this.generator.nextPoint());
	}
}
