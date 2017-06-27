package org.iMage.geometrify.generators;

import org.iMage.geometrify.primitives.Ellipse;
import org.iMage.geometrify.primitives.Primitive;

public class EllipseGenerator extends AbstractPrimitiveGenerator {

	public EllipseGenerator(int width, int height) {
		this(new RandomPointGenerator(width, height));
	}

	public EllipseGenerator(RandomPointGenerator generator) {
		super(generator);
	}

	@Override
	public Primitive generatePrimitive() {
		return new Ellipse(this.generator.nextPoint(), this.generator.nextPoint());
	}

	@Override
	protected PrimitiveGenerator createBy(RandomPointGenerator generator) {
		return new EllipseGenerator(generator);
	}
}
