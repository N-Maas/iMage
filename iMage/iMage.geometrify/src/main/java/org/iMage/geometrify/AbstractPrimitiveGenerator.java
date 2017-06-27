package org.iMage.geometrify;

public abstract class AbstractPrimitiveGenerator implements BindablePrimitiveGenerator {
	protected final RandomPointGenerator generator;

	public AbstractPrimitiveGenerator(RandomPointGenerator generator) {
		this.generator = generator;
	}

	@Override
	public BoundedPrimitiveGenerator bindToArea(int x, int y, int width, int height, Bounds bounds) {
		int genX = Math.min(this.getMinX() + this.getWidth(), x);
		int genY = Math.min(this.getMinY() + this.getHeight(), y);
		int genWidth = Math.min(this.getMinX() + this.getWidth() - genX, width);
		int genHeight = Math.min(this.getMinY() + this.getHeight() - genY, height);
		return new BoundedPrimitiveGenerator(this::createBy, genX, genY, genWidth, genHeight, bounds);
	}

	protected abstract PrimitiveGenerator createBy(RandomPointGenerator generator);

	@Override
	public int getWidth() {
		return this.generator.getWidth();
	}

	@Override
	public int getHeight() {
		return this.generator.getHeight();
	}

	@Override
	public int getMinX() {
		return this.generator.getMinX();
	}

	@Override
	public int getMinY() {
		return this.generator.getMinY();
	}
}
