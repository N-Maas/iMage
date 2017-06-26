package org.iMage.geometrify;

import java.awt.Rectangle;
import java.util.BitSet;

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
		return this.insideFlags[x].get(y);
	}

	@Override
	public boolean isInsideBounds(int x, int y) {
		return x < this.getMinX() || x >= this.getMinX() + this.getWidth() || y < this.getMinY()
				|| y >= this.getMinY() + this.getHeight();
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
		BitSet[] result = new BitSet[this.getHeight()];
		for (int i = 0; i < result.length; i++) {
			result[i] = new BitSet(this.getWidth());
		}
		int[] points = this.getInsidePoints();
		for (int i = 0; i < points.length; i += 2) {
			result[points[i]].set(points[i + 1]);
		}
		return result;
	}
}
