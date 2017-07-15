package org.iMage.geometrify.primitives;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A polygon.
 *
 * @author Nikolai Maas
 * @version 1.0
 */
public class FPolygon extends AbstractPrimitive {
	private final List<Point> points;
	private List<Point> insidePoints;

	public FPolygon(Point... points) {
		super(calculateBounds(points));
		this.points = new ArrayList<>();
		for (Point p : points) {
			this.points.add(p);
		}
	}

	private static Rectangle calculateBounds(Point... points) {
		if (points.length < 3) {
			throw new IllegalArgumentException("Illegal point number.");
		}
		int minX = Integer.MAX_VALUE;
		int maxX = 0;
		int minY = Integer.MAX_VALUE;
		int maxY = 0;
		for (Point point : points) {
			minX = Math.min(minX, point.x);
			maxX = Math.max(maxX, point.x);
			minY = Math.min(minY, point.y);
			maxY = Math.max(maxY, point.y);
		}
		return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.iMage.geometrify.AbstractPrimitive#calculatePoints()
	 */
	@Override
	protected int[] calculatePoints() {
		byte[][] flags = null;
		flags = new byte[this.getWidth()][this.getHeight()];
		for (int i = 0; i < flags.length; i++) {
			flags[i] = new byte[this.getHeight()];
			Arrays.fill(flags[i], (byte) 0);
		}

		// draws "lines" from point to point
		Point last = this.points.get(this.points.size() - 1);
		for (Point p : this.points) {
			Point up = last.y < p.y ? last : p;
			Point down = last.y < p.y ? p : last;
			int upX = up.x - this.getMinX();
			int minY = up.y - this.getMinY();
			int dX = down.x - up.x;
			int height = down.y - up.y;

			for (int i = 0; i < height; i++) {
				flags[upX + (int) Math.round((double) (dX * i) / height)][minY + i]++;
			}
			last = p;
		}

		// iterates through the pixels and "fills" the polygon
		int[] puffer = new int[2 * this.getWidth() * this.getHeight()];
		int counter = 0;
		for (int y = 0; y < this.getHeight(); y++) {
			boolean contained = false;
			boolean add;
			for (int x = 0; x < this.getWidth(); x++) {
				add = false;
				if (flags[x][y] != 0) {
					add = true;
					contained ^= (flags[x][y] & 1) != 0;
				} else if (contained) {
					add = true;
				}
				if (add) {
					puffer[counter] = x + this.getMinX();
					puffer[counter + 1] = y + this.getMinY();
					counter += 2;
				}
			}
		}

		return Arrays.copyOf(puffer, counter);
	}

	@Override
	public String toString() {
		return "FPolygon[" + this.points + "]";
	}
}
