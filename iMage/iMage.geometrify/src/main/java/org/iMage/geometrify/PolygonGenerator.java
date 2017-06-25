package org.iMage.geometrify;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class PolygonGenerator implements IPrimitiveGenerator {
	public static final int RANDOM_BOUNDS = -42, NO_BOUNDS = -43;
	private final int width;
	private final int height;
	private final int minPoints;
	private final int maxPoints;
	private int bounds = NO_BOUNDS;

	public PolygonGenerator(int width, int height) {
		this(width, height, 3, 3);
	}

	public PolygonGenerator(int width, int height, int points) {
		this(width, height, points, points);
	}

	public PolygonGenerator(int width, int height, int minPoints, int maxPoints) {
		if (minPoints < 3 || maxPoints < minPoints) {
			throw new IllegalArgumentException("Illegal point range.");
		}
		this.width = width;
		this.height = height;
		this.minPoints = minPoints;
		this.maxPoints = maxPoints;
	}

	@Override
	public IPrimitive generatePrimitive() {
		RandomPointGenerator generator;
		if (this.bounds == NO_BOUNDS) {
			generator = new RandomPointGenerator(this.width, this.height);
		} else if (this.bounds == RANDOM_BOUNDS) {
			generator = new RandomPointGenerator(this.width, this.height);
			Point a = generator.nextPoint();
			Point b;
			do {
				b = generator.nextPoint();
			} while (Math.abs(a.x - b.x) < 2 || Math.abs(a.y - b.y) < 2);
			generator = new RandomPointGenerator(Math.min(a.x, b.x), Math.min(a.y, b.y), Math.abs(a.x - b.x),
					Math.abs(a.y - b.y));
		} else {
			generator = new RandomPointGenerator(this.width - this.bounds, this.height - this.bounds);
			Point a = generator.nextPoint();
			generator = new RandomPointGenerator(a.x, a.y, this.bounds, this.bounds);
		}

		int pointCount = this.minPoints + (int) ((this.maxPoints - this.minPoints + 1) * Math.random());
		List<Point> points = new ArrayList<>(pointCount);
		for (int i = 0; i < pointCount; i++) {
			points.add(generator.nextPoint());
		}
		return new IPolygon(this.reorder(points));
	}

	public int getBounds() {
		return this.bounds;
	}

	public void setBounds(int bounds) {
		if (bounds < 2 && bounds != RANDOM_BOUNDS && bounds != NO_BOUNDS) {
			throw new IllegalArgumentException("Illegal bounds value.");
		}
		this.bounds = bounds;
	}

	private Point[] reorder(List<Point> points) {
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxY = Integer.MIN_VALUE;

		for (Point p : points) {
			minX = Math.min(minX, p.x);
			maxX = Math.max(maxX, p.x);
			minY = Math.min(minY, p.y);
			maxY = Math.max(maxY, p.y);
		}
		int avgX = (maxX + minX) / 2;
		int avgY = (maxY + minY) / 2;

		SortedMap<Double, List<Point>> order = new TreeMap<>();
		for (Point point : points) {
			double value = 0;
			int delX = point.x - avgX;
			int delY = point.y - avgY;
			if (!(delX == 0 && delY == 0)) {
				if (delX >= 0) {
					value = Math.acos((-delY) / Math.sqrt(delX * delX + delY * delY));
				} else {
					value = Math.PI + Math.acos((delY) / Math.sqrt(delX * delX + delY * delY));
				}
			}
			List<Point> addPoints = new ArrayList<>();
			addPoints.add(point);
			order.merge(value, addPoints, (s1, s2) -> {
				s1.addAll(s2);
				return s1;
			});
		}
		return order.values().stream().reduce((s1, s2) -> {
			s1.addAll(s2);
			return s1;
		}).get().toArray(new Point[0]);
	}
}