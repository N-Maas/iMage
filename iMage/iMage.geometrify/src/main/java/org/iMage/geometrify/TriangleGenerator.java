package org.iMage.geometrify;

public class TriangleGenerator extends AbstractPrimitiveGenerator {

	public TriangleGenerator(int width, int height) {
		this(new RandomPointGenerator(width, height));
	}

	public TriangleGenerator(RandomPointGenerator generator) {
		super(generator);
	}

	@Override
	public Primitive generatePrimitive() {
		return new Triangle(this.generator.nextPoint(), this.generator.nextPoint(), this.generator.nextPoint());
	}

	@Override
	protected PrimitiveGenerator createBy(RandomPointGenerator generator) {
		return new TriangleGenerator(generator);
	}
}
