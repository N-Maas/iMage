package org.iMage.geometrify;

import java.util.function.Function;

public class BoundedPrimitiveGenerator implements PrimitiveGenerator {
	private final Function<RandomPointGenerator, PrimitiveGenerator> genFactory;
	private final int x;
	private final int y;
	private final int width;
	private final int height;
	private Bounds bounds;

	public BoundedPrimitiveGenerator(Function<RandomPointGenerator, PrimitiveGenerator> genFactory, int width,
			int height) {
		this(genFactory, width, height, Bounds.NO_BOUNDS);
	}

	public BoundedPrimitiveGenerator(Function<RandomPointGenerator, PrimitiveGenerator> genFactory, int width,
			int height, Bounds bounds) {
		this(genFactory, 0, 0, width, height, bounds);
	}

	public BoundedPrimitiveGenerator(Function<RandomPointGenerator, PrimitiveGenerator> genFactory, int x, int y,
			int width, int height) {
		this(genFactory, x, y, width, height, Bounds.NO_BOUNDS);
	}

	public BoundedPrimitiveGenerator(Function<RandomPointGenerator, PrimitiveGenerator> genFactory, int x, int y,
			int width, int height, Bounds bounds) {
		this.genFactory = genFactory;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bounds = bounds;
	}

	@Override
	public Primitive generatePrimitive() {
		return this.genFactory.apply(this.bounds.bind(this.x, this.y, this.width, this.height)).generatePrimitive();
	}

	public Bounds getBounds() {
		return this.bounds;
	}

	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getMinX() {
		return this.x;
	}

	@Override
	public int getMinY() {
		return this.y;
	}

}
