package org.iMage.geometrify.generators;

import org.iMage.geometrify.primitives.FRectangle;
import org.iMage.geometrify.primitives.Primitive;

public class RectangleGenerator extends AbstractPrimitiveGenerator {

	public RectangleGenerator(int width, int height) {
		this(new RandomPointGenerator(width, height));
	}

	public RectangleGenerator(RandomPointGenerator generator) {
		super(generator);
	}

	@Override
	public Primitive generatePrimitive() {
		return new FRectangle(this.generator.nextPoint(), this.generator.nextPoint());
	}

	@Override
	protected PrimitiveGenerator createBy(RandomPointGenerator generator) {
		return new RectangleGenerator(generator);
	}
}
