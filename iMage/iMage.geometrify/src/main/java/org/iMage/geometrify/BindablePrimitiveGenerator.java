package org.iMage.geometrify;

public interface BindablePrimitiveGenerator extends PrimitiveGenerator {

	default BoundedPrimitiveGenerator bind(Bounds bounds) {
		return this.bindToArea(this.getMinX(), this.getMinY(), this.getWidth(), this.getHeight(), bounds);
	}

	default BoundedPrimitiveGenerator bindToArea(int x, int y, int width, int height) {
		return this.bindToArea(x, y, width, height, Bounds.NO_BOUNDS);

	}

	BoundedPrimitiveGenerator bindToArea(int x, int y, int width, int height, Bounds bounds);
}
