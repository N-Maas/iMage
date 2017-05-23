package org.iMage.geometrify;

import java.awt.Point;

/**
 * Provides an infinite source of points at random coordinates within a given
 * range.
 *
 * @author Dominic Ziegler
 * @version 1.0
 */
public class RandomPointGenerator implements IPointGenerator {
	private final int width;
	private final int height;

	/**
	 * Constructs the generator for points within the specified coordinate
	 * space.
	 *
	 * @param width
	 *            the maximum x coordinate
	 * @param height
	 *            the maximum y coordinate
	 */
	public RandomPointGenerator(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public Point nextPoint() {
		return new Point((int) (Math.random() * this.width), (int) (Math.random() * this.height));
	}
}
