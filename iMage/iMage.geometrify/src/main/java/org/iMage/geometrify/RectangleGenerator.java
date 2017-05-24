package org.iMage.geometrify;

public class RectangleGenerator implements IPrimitiveGenerator {
	private final RandomPointGenerator generator;

	public RectangleGenerator(int width, int height) {
		this.generator = new RandomPointGenerator(width, height);
	}

	@Override
	public IPrimitive generatePrimitive() {
		return new IRectangle(this.generator.nextPoint(), this.generator.nextPoint());
	}
}
