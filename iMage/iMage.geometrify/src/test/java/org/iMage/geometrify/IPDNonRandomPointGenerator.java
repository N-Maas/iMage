package org.iMage.geometrify;

import java.awt.Point;
import java.util.Random;

/**
 * Provides an infinite source of points at random coordinates within a given
 * range.
 * 
 * @author Dominic Ziegler, Martin Blersch
 * @version 1.0
 */
public class IPDNonRandomPointGenerator implements IPointGenerator {
	private int width;
	private int height;
	private Random rand = new Random(1000000L);

	/**
	 * Constructs the generator for points within the specified coordinate
	 * space.
	 * 
	 * @param width
	 *            the maximum x coordinate
	 * @param height
	 *            the maximum y coordinate
	 */
	public IPDNonRandomPointGenerator(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public Point nextPoint() {
		return new Point(rand.nextInt(width), rand.nextInt(height));
	}
}