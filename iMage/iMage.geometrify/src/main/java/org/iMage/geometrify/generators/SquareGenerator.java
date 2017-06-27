package org.iMage.geometrify.generators;

import java.awt.Point;

import org.iMage.geometrify.primitives.FRectangle;
import org.iMage.geometrify.primitives.Primitive;

public class SquareGenerator extends AbstractPrimitiveGenerator {

	public SquareGenerator(int width, int height) {
		this(new RandomPointGenerator(width, height));
	}

	public SquareGenerator(RandomPointGenerator generator) {
		super(generator);
	}

	@Override
	public Primitive generatePrimitive() {
		Point a = this.generator.nextPoint();
		Point b = this.generator.nextPoint();
		int width = this.calculateWidth(a, b);
		a = new Point(Math.min(a.x, b.x), Math.min(a.y, b.y));
		b = new Point(a.x + width - 1, a.y + width - 1);
		return new FRectangle(a, b);
	}

	@Override
	protected PrimitiveGenerator createBy(RandomPointGenerator generator) {
		return new SquareGenerator(generator);
	}
}
