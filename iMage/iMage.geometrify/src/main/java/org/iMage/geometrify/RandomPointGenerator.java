package org.iMage.geometrify;

import java.awt.Point;

public class RandomPointGenerator implements IPointGenerator {
	private final int width;
	private final int height;
	private final int minX;
	private final int maxX;

	/**
	 * Constructs the generator for points within the specified coordinate the
	 * maximum y coordinate
	 */
	public RandomPointGenerator(int width, int height) {
		this(0, 0, width, height);
	}

	public RandomPointGenerator(int minX, int maxX, int width, int height) {
		if (width < 1 || height < 1 || minX < 0 || maxX < 0) {
			throw new IllegalArgumentException("Illegal bounds.");
		}
		this.width = width;
		this.height = height;
		this.minX = minX;
		this.maxX = maxX;
	}

	@Override
	public Point nextPoint() {
		return new Point(this.minX + (int) (Math.random() * this.width),
				this.maxX + (int) (Math.random() * this.height));
	}
}