package org.iMage.geometrify.generators;

import org.iMage.geometrify.primitives.Primitive;
import org.iMage.geometrify.primitives.Triangle;

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
