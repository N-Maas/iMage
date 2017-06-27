package org.iMage.geometrify.generators;

import java.awt.Point;
import java.util.Random;

import org.iMage.geometrify.primitives.FPolygon;
import org.iMage.geometrify.primitives.Primitive;

public class StarGenerator extends AbstractPrimitiveGenerator {
	private final Random r = new Random();
	private final int minPoints;
	private final int maxPoints;
	private double innerRadian = 0.5;
	private double innerVariance = 0;
	private double outerVariance = 0;
	private double arcVariance = 0;

	public StarGenerator(int width, int height) {
		this(new RandomPointGenerator(width, height));
	}

	public StarGenerator(RandomPointGenerator generator) {
		this(generator, 5, 5);
	}

	public StarGenerator(int width, int height, int minPoints, int maxPoints) {
		this(new RandomPointGenerator(width, height), minPoints, maxPoints);
	}

	public StarGenerator(RandomPointGenerator generator, int minPoints, int maxPoints) {
		this(generator, minPoints, maxPoints, 0.5, 0, 0, 0);
	}

	public StarGenerator(RandomPointGenerator generator, int minPoints, int maxPoints, double innerRadian,
			double innerVariance, double outerVariance, double arcVariance) {
		super(generator);
		if (minPoints < 2 || maxPoints < minPoints) {
			throw new IllegalArgumentException("Illegal point range.");
		}
		this.minPoints = minPoints;
		this.maxPoints = maxPoints;
		this.innerRadian = innerRadian;
		this.innerVariance = innerVariance;
		this.outerVariance = outerVariance;
		this.arcVariance = arcVariance;
	}

	@Override
	public Primitive generatePrimitive() {
		Point a = this.generator.nextPoint();
		Point b = this.generator.nextPoint();
		int x = Math.min(a.x, b.x);
		int y = Math.min(a.y, b.y);
		int width = this.calculateWidth(a, b);
		int radian = (width - 1) / 2;
		int midX = x + radian;
		int midY = y + radian;

		int pointCount = this.minPoints + this.r.nextInt(this.maxPoints - this.minPoints + 1);
		double arc = Math.PI / pointCount;
		Point[] points = new Point[pointCount * 2];
		for (int i = 0; i < points.length; i += 2) {
			points[i] = rotateVector(0, (int) Math.round(radian * (1 - this.r.nextFloat() * this.outerVariance)),
					arc * (i + this.nextVariance(this.arcVariance)));
			points[i].translate(midX, midY);
			double limitedVar = Math.min(this.innerVariance, (1 - this.innerRadian) / this.innerRadian);
			points[i + 1] = rotateVector(0,
					(int) Math.round(radian * (1 + this.nextVariance(limitedVar)) * this.innerRadian),
					arc * (i + 1 + this.nextVariance(this.arcVariance)));
			points[i + 1].translate(midX, midY);
		}
		return new FPolygon(points);
	}

	@Override
	protected PrimitiveGenerator createBy(RandomPointGenerator generator) {
		return new StarGenerator(generator, this.minPoints, this.maxPoints, this.innerRadian, this.innerVariance,
				this.outerVariance, this.arcVariance);
	}

	public double getInnerRadian() {
		return this.innerRadian;
	}

	public void setInnerRadian(double innerRadian) {
		if (innerRadian < 0 || innerRadian > 1) {
			throw new IllegalArgumentException("Inner radian out of bounds: [0, 1]");
		}
		this.innerRadian = innerRadian;
	}

	public double getInnerVariance() {
		return this.innerVariance;
	}

	public void setInnerVariance(double innerVariance) {
		if (innerVariance < 0 || innerVariance > 1) {
			throw new IllegalArgumentException("Inner variance out of bounds: [0, 1]");
		}
		this.innerVariance = innerVariance;
	}

	public double getOuterVariance() {
		return this.outerVariance;
	}

	public void setOuterVariance(double outerVariance) {
		if (outerVariance < 0 || outerVariance > 1) {
			throw new IllegalArgumentException("Outer variance out of bounds: [0, 1]");
		}
		this.outerVariance = outerVariance;
	}

	public double getArcVariance() {
		return this.arcVariance;
	}

	public void setArcVariance(double arcVariance) {
		if (arcVariance < 0 || arcVariance > 1) {
			throw new IllegalArgumentException("Arc variance out of bounds: [0, 1]");
		}
		this.arcVariance = arcVariance;
	}

	private static Point rotateVector(int x, int y, double arc) {
		double cos = Math.cos(arc);
		double sin = Math.sin(arc);
		return new Point((int) Math.round(x * cos + y * sin), (int) Math.round(y * cos - x * sin));
	}

	private double nextVariance(double variance) {
		return (2 * this.r.nextDouble() - 1) * variance;
	}

	public static void main(String[] args) {
		StarGenerator sg = new StarGenerator(100, 100);
		System.out.println(sg.calculateWidth(new Point(20, 20), new Point(60, 60)));
		System.out.println(sg.calculateWidth(new Point(20, 80), new Point(60, 90)));
	}
}
