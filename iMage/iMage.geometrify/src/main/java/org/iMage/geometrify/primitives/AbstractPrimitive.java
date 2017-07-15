package org.iMage.geometrify.primitives;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.BitSet;
import java.util.function.BiPredicate;

public abstract class AbstractPrimitive implements Primitive {
	private final int minX;
	private final int minY;
	private final int width;
	private final int height;
	private int[] insidePoints;
	private BitSet[] insideFlags;

	/**
	 * IMPORTANT: maxX - minX != width = maxX - minX + 1
	 * 
	 * @param minX
	 * @param minY
	 * @param width
	 * @param height
	 */
	public AbstractPrimitive(int minX, int minY, int width, int height) {
		this.minX = minX;
		this.minY = minY;
		this.width = width;
		this.height = height;
	}

	public AbstractPrimitive(Rectangle r) {
		this(r.x, r.y, (int) r.getWidth(), (int) r.getHeight());
	}

	protected int[] buildPoints(BiPredicate<Integer, Integer> predicate) {
		int[] result = new int[2 * this.getWidth() * this.getHeight()];
		int index = 0;
		for (int y = this.getMinY(); y < this.getMinY() + this.getHeight(); y++) {
			for (int x = this.getMinX(); x < this.getMinX() + this.getWidth(); x++) {
				if (predicate.test(x, y)) {
					result[index] = x;
					result[index + 1] = y;
					index += 2;
				}
			}
		}
		return index < result.length ? Arrays.copyOf(result, index) : result;
	}

	protected int[] buildPointsByOrigin(BiPredicate<Integer, Integer> predicate) {
		int[] result = new int[2 * this.getWidth() * this.getHeight()];
		int index = 0;
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {
				if (predicate.test(x, y)) {
					result[index] = x + this.getMinX();
					result[index + 1] = y + this.getMinY();
					index += 2;
				}
			}
		}
		return index < result.length ? Arrays.copyOf(result, index) : result;
	}

	/**
	 * Creates a list of all points that are inside the specified primitive. The
	 * calculation is cached and defined by the subclasses.
	 * 
	 * @param ip
	 *            the primitive
	 * @return list of all points inside the primitive
	 */
	@Override
	public int[] getInsidePoints() {
		if (this.insidePoints == null) {
			this.insidePoints = this.calculatePoints();
		}
		return this.insidePoints;
	}

	@Override
	public boolean isInsidePrimitive(int x, int y) {
		if (!this.isInsideBounds(x, y)) {
			return false;
		}
		if (this.insideFlags == null) {
			this.insideFlags = this.calculateFlags();
		}
		return this.insideFlags[x - this.getMinX()].get(y - this.getMinY());
	}

	@Override
	public boolean isInsideBounds(int x, int y) {
		return !(x < this.getMinX() || x >= this.getMinX() + this.getWidth() || y < this.getMinY()
				|| y >= this.getMinY() + this.getHeight());
	}

	@Override
	public int getMinX() {
		return this.minX;
	}

	@Override
	public int getMinY() {
		return this.minY;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	protected abstract int[] calculatePoints();

	protected BitSet[] calculateFlags() {
		BitSet[] result = new BitSet[this.getWidth()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new BitSet(this.getHeight());
		}
		int[] points = this.getInsidePoints();
		for (int i = 0; i < points.length; i += 2) {
			result[points[i] - this.getMinX()].set(points[i + 1] - this.getMinY());
		}
		return result;
	}
}
