package org.iMage.geometrify;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Modifies an image by iteratively reconstructing it with triangles.
 *
 * @version 1.0
 */
public class TrianglePictureFilter extends AbstractPrimitivePictureFilter {
	private static final int RED_SPACE = 255;
	private static final int GREEN_SPACE = 255 << 8;
	private static final int BLUE_SPACE = 255 << 16;
	private static final int ALPHA_SPACE = 255 << 24;

	/**
	 * Constructs the filter within the specified point generator.
	 *
	 * @param pointGenerator
	 *            the pointGenerator
	 */
	public TrianglePictureFilter(IPointGenerator pointGenerator) {
		super(pointGenerator);
	}

	@Override
	protected Color calculateColor(BufferedImage image, IPrimitive primitive) {
		boolean hasAlpha = image.getColorModel().hasAlpha();
		long red = 0;
		long green = 0;
		long blue = 0;
		long alpha = 0;
		List<Point> points = TrianglePictureFilter.calculateInsidePoints(primitive);
		if (points.size() == 0) {
			return Color.BLACK;
		}

		for (Point p : points) {
			Color c = new Color(image.getRGB(p.x, p.y), hasAlpha);
			red += c.getRed();
			green += c.getGreen();
			blue += c.getBlue();
			alpha += c.getAlpha();
		}
		// System.out.println("r: " + red / points.size() + " g: " + green /
		// points.size() + " b: "
		// + blue / points.size() + " a: " + alpha / points.size());
		return new Color((int) (red / points.size()), (int) (green / points.size()), (int) (blue / points.size()),
				(int) (alpha / points.size()));
	}

	@Override
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples) {
		if (numberOfIterations < 1 || numberOfSamples < 1) {
			throw new IllegalArgumentException("Numbers must be greater 0.");
		}

		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		for (int i = 0; i < numberOfIterations; i++) {
			int minDiff = Integer.MAX_VALUE;
			IPrimitive minPrimitive = null;
			for (int j = 0; j < numberOfSamples; j++) {
				IPrimitive testPrim = this.generatePrimitive();
				testPrim.setColor(this.calculateColor(image, testPrim));
				int diff = this.calculateDifference(image, newImage, testPrim);
				if (diff < minDiff) {
					minDiff = diff;
					minPrimitive = testPrim;
				}
			}
			this.addToImage(newImage, minPrimitive);
		}
		return newImage;
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
		boolean hasAlpha = original.getColorModel().hasAlpha();
		List<Point> points = TrianglePictureFilter.calculateInsidePoints(primitive);
		int difference = 0;
		for (Point p : points) {
			Color orgColor = new Color(original.getRGB(p.x, p.y), hasAlpha);
			Color oldColor = new Color(current.getRGB(p.x, p.y), hasAlpha);
			Color newColor = colorAverage(primitive.getColor(), oldColor);
			difference += colorDifference(orgColor, newColor) - colorDifference(orgColor, oldColor);
		}
		return difference;
	}

	@Override
	protected void addToImage(BufferedImage image, IPrimitive primitive) {
		boolean hasAlpha = image.getColorModel().hasAlpha();
		List<Point> points = TrianglePictureFilter.calculateInsidePoints(primitive);
		for (Point p : points) {
			image.setRGB(p.x, p.y,
					colorAverage(primitive.getColor(), new Color(image.getRGB(p.x, p.y), hasAlpha)).getRGB());
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

	private static Color colorAverage(Color a, Color b) {
		return new Color((a.getRed() + b.getRed()) / 2, (a.getGreen() + b.getGreen()) / 2,
				(a.getBlue() + b.getBlue()) / 2, (a.getAlpha() + b.getAlpha()) / 2);
	}

	private static int colorDifference(Color a, Color b) {
		int red = Math.abs(a.getRed() - b.getRed());
		int green = Math.abs(a.getGreen() - b.getGreen());
		int blue = Math.abs(a.getBlue() - b.getBlue());
		int alpha = Math.abs(a.getAlpha() - b.getAlpha());
		return red + green + blue + alpha;
	}
}
