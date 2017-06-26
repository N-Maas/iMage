package org.iMage.geometrify;

import java.awt.Point;
import java.util.Random;

public class RandomPointGenerator implements IPointGenerator {
	private final Random r = new Random();
	private final int width;
	private final int height;
	private final int minX;
	private final int minY;

	/**
	 * Constructs the generator for points within the specified coordinate the
	 * maximum y coordinate
	 */
	public RandomPointGenerator(int width, int height) {
		this(0, 0, width, height);
	}

	public RandomPointGenerator(int minX, int minY, int width, int height) {
		if (width < 1 || height < 1 || minX < 0 || minY < 0) {
			throw new IllegalArgumentException("Illegal bounds.");
		}
		this.width = width;
		this.height = height;
		this.minX = minX;
		this.minY = minY;
	}

	@Override
	public Point nextPoint() {
		return new Point(this.minX + this.r.nextInt(this.width), this.minY + this.r.nextInt(this.height));
	}
}