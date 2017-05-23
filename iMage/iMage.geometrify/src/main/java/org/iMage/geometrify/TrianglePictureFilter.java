package org.iMage.geometrify;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TrianglePictureFilter extends AbstractPrimitivePictureFilter {
	private static final int RED_SPACE = 255;
	private static final int GREEN_SPACE = 255 << 8;
	private static final int BLUE_SPACE = 255 << 16;
	private static final int ALPHA_SPACE = 255 << 24;

	public TrianglePictureFilter(IPointGenerator pointGenerator) {
		super(pointGenerator);
	}

	@Override
	protected Color calculateColor(BufferedImage image, IPrimitive primitive) {
		long red = 0;
		long green = 0;
		long blue = 0;
		long alpha = 0;
		List<Point> points = TrianglePictureFilter.calculateInsidePoints(primitive);

		for (Point p : points) {
			long argb = Integer.toUnsignedLong(image.getRGB(p.x, p.y));
			red += argb & RED_SPACE;
			green += argb & GREEN_SPACE;
			blue += argb & BLUE_SPACE;
			alpha += argb & ALPHA_SPACE;
		}
		return new Color((int) (red / points.size() + (green / points.size() & GREEN_SPACE)
				+ (blue / points.size() & BLUE_SPACE) + (alpha / points.size() & ALPHA_SPACE)),
				image.getColorModel().hasAlpha());
	}

	@Override
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples) {
		/*
		 * YOUR SOLUTION HERE
		 */
		return null;
	}

	@Override
	protected IPrimitive generatePrimitive() {
		Point a = super.pointGenerator.nextPoint();
		Point b = super.pointGenerator.nextPoint();
		Point c = super.pointGenerator.nextPoint();
		return new Triangle(a, b, c);
	}

	@Override
	protected int calculateDifference(BufferedImage original, BufferedImage current, IPrimitive primitive) {
		List<Point> points = TrianglePictureFilter.calculateInsidePoints(primitive);
		int difference = 0;
		for (Point p : points) {
			int oldColor = current.getRGB(p.x, p.y);
			int newColor = colorAverage(primitive.getColor(), oldColor);
			int orgColor = original.getRGB(p.x, p.y);
			difference += colorDifference(orgColor, newColor) - colorDifference(orgColor, oldColor);
		}
		return difference;
	}

	@Override
	protected void addToImage(BufferedImage image, IPrimitive primitive) {
		List<Point> points = TrianglePictureFilter.calculateInsidePoints(primitive);
		for (Point p : points) {
			image.setRGB(p.x, p.y, colorAverage(primitive.getColor(), image.getRGB(p.x, p.y)));
		}
	}

	private static List<Point> calculateInsidePoints(IPrimitive ip) {
		BoundingBox bb = ip.getBoundingBox();
		int minX = bb.getUpperLeftCorner().x;
		int minY = bb.getUpperLeftCorner().y;
		int maxX = bb.getLowerRightCorner().x;
		int maxY = bb.getLowerRightCorner().y;
		List<Point> result = new ArrayList<>((maxX - minX + 1) * (maxY - minY + 1) / 2 + 1);

		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				Point p = new Point(i, j);
				if (ip.isInsidePrimitive(p)) {
					result.add(p);
				}
			}
		}
		return result;
	}

	private static int colorAverage(Color c, int rgb) {
		int red = (c.getRed() + (rgb & RED_SPACE)) >> 1;
		int green = ((c.getGreen() << 7) + ((rgb & GREEN_SPACE) >>> 1)) & GREEN_SPACE;
		int blue = ((c.getBlue() << 15) + ((rgb & BLUE_SPACE) >>> 1)) & BLUE_SPACE;
		int alpha = ((c.getAlpha() << 23) + ((rgb & ALPHA_SPACE) >>> 1)) & ALPHA_SPACE;
		return red + green + blue + alpha;
	}

	private static int colorDifference(int a, int b) {
		int red = Math.abs((a & RED_SPACE) - (b & RED_SPACE));
		int green = Math.abs((a & GREEN_SPACE) - (b & GREEN_SPACE)) >> 8;
		int blue = Math.abs((a & BLUE_SPACE) - (b & BLUE_SPACE)) >> 16;
		int alpha = (int) (Math
				.abs((Integer.toUnsignedLong(a) & ALPHA_SPACE) - (Integer.toUnsignedLong(b) & ALPHA_SPACE)) >> 24);
		return red + green + blue + alpha;
	}
}
