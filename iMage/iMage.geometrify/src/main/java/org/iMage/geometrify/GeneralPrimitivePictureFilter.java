package org.iMage.geometrify;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Modifies an image by iteratively reconstructing it with triangles.
 *
 * @author Nikolai
 */
public class GeneralPrimitivePictureFilter implements IPrimitivePictureFilter {
	private final IPrimitiveGenerator generator;
	private final float opaque;

	/**
	 * Constructs the filter within the specified point generator.
	 *
	 * @param pointGenerator
	 *            the pointGenerator
	 */
	public GeneralPrimitivePictureFilter(IPrimitiveGenerator generator) {
		this(generator, 0.5f);
	}

	/**
	 * Constructs the filter within the specified point generator.
	 *
	 * @param pointGenerator
	 *            the pointGenerator
	 */
	public GeneralPrimitivePictureFilter(IPrimitiveGenerator generator, float opaque) {
		this.generator = generator;
		this.opaque = opaque;
	}

	private static Color calculateColor(int[] data, IPrimitive primitive, int width, boolean hasAlpha) {
		List<Point> insidePoints = primitive.getInsidePoints();
		long red = 0;
		long green = 0;
		long blue = 0;
		long alpha = 0;
		if (insidePoints.size() == 0) {
			return Color.BLACK;
		}

		for (Point p : insidePoints) {
			Color c = new Color(data[calcIndex(p.x, p.y, width)], hasAlpha);
			red += c.getRed();
			green += c.getGreen();
			blue += c.getBlue();
			alpha += c.getAlpha();
		}
		return new Color((int) (red / insidePoints.size()), (int) (green / insidePoints.size()),
				(int) (blue / insidePoints.size()), (int) (alpha / insidePoints.size()));
	}

	@Override
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples) {
		if (numberOfIterations < 1 || numberOfSamples < 1) {
			throw new IllegalArgumentException("Numbers must be greater 0.");
		}

		int width = image.getWidth();
		boolean hasAlpha = image.getColorModel().hasAlpha();
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		int[] orgSample = new int[width * image.getHeight()];
		image.getRaster().getPixel(width - 1, image.getHeight() - 1, orgSample);

		// reuseable resources
		ExecutorService executor = Executors.newFixedThreadPool(numberOfSamples);
		IPrimitive[] prims = new IPrimitive[numberOfSamples];
		Color[] colors = new Color[numberOfSamples];
		int[] diffs = new int[numberOfSamples];
		ReentrantLock lock = new ReentrantLock();

		for (int i = 0; i < numberOfIterations; i++) {
			CountDownLatch latch = new CountDownLatch(numberOfSamples);
			int[] newSample = new int[newImage.getWidth() * newImage.getHeight()];
			newImage.getRaster().getPixel(newImage.getWidth() - 1, newImage.getHeight() - 1, newSample);
			for (int j = 0; j < numberOfSamples; j++) {
				final int puffer = j;
				executor.submit(() -> {
					try {
						prims[puffer] = this.generator.generatePrimitive();
						colors[puffer] = GeneralPrimitivePictureFilter.calculateColor(orgSample, prims[puffer], width,
								hasAlpha);
						diffs[puffer] = this.calculateDifference(orgSample, newSample, colors[puffer], prims[puffer],
								width, hasAlpha);
						latch.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new RuntimeException("Synchronisation failure in calculation.");
			}

			int min = Integer.MAX_VALUE;
			int minIndex = 0;
			for (int k = 0; k < numberOfSamples; k++) {
				if (diffs[k] <= min) {
					min = diffs[k];
					minIndex = k;
				}
			}

			this.addToImage(newImage, colors[minIndex], prims[minIndex]);
		}
		return newImage;
	}

	/*
	 * This method performance-optimized by firstly not copying the picture and
	 * inserting the primitive, but directly calculating the difference.
	 * Secondly, the difference is calculated relative to the difference of the
	 * current image (current difference = 0). This reduces the calculation
	 * effort to the bounding box. Therefore, most results are probably
	 * negative.
	 * 
	 * @see
	 * org.iMage.geometrify.AbstractPrimitivePictureFilter#calculateDifference(
	 * java.awt.image.BufferedImage, java.awt.image.BufferedImage,
	 * org.iMage.geometrify.IPrimitive)
	 */

	// private int calculateDifference(BufferedImage original, BufferedImage
	// current, Color color, IPrimitive primitive) {
	// List<Point> insidePoints = primitive.getInsidePoints();
	// boolean hasAlpha = original.getColorModel().hasAlpha();
	// int difference = 0;
	// for (Point p : insidePoints) {
	// Color orgColor = new Color(original.getRGB(p.x, p.y), hasAlpha);
	// Color oldColor = new Color(current.getRGB(p.x, p.y), hasAlpha);
	// Color newColor = colorAverage(color, oldColor, this.opaque);
	// difference += colorDifference(orgColor, newColor) -
	// colorDifference(orgColor, oldColor);
	// }
	// return difference;
	// }

	private int calculateDifference(int[] orgData, int[] newData, Color color, IPrimitive primitive, int width,
			boolean hasAlpha) {
		List<Point> insidePoints = primitive.getInsidePoints();
		int difference = 0;
		for (Point p : insidePoints) {
			Color orgColor = new Color(orgData[GeneralPrimitivePictureFilter.calcIndex(p.x, p.y, width)], hasAlpha);
			Color oldColor = new Color(newData[GeneralPrimitivePictureFilter.calcIndex(p.x, p.y, width)], hasAlpha);
			Color newColor = colorAverage(color, oldColor, this.opaque);
			difference += colorDifference(orgColor, newColor) - colorDifference(orgColor, oldColor);
		}
		return difference;
	}

	protected void addToImage(BufferedImage image, Color color, IPrimitive primitive) {
		List<Point> insidePoints = primitive.getInsidePoints();
		boolean hasAlpha = image.getColorModel().hasAlpha();
		for (Point p : insidePoints) {
			image.setRGB(p.x, p.y,
					colorAverage(color, new Color(image.getRGB(p.x, p.y), hasAlpha), this.opaque).getRGB());
		}
	}

	private static Color colorAverage(Color orgC, Color newC, float opaque) {
		float inverse = 1 - opaque;
		int red = (int) (orgC.getRed() * inverse + newC.getRed() * opaque);
		int green = (int) (orgC.getGreen() * inverse + newC.getGreen() * opaque);
		int blue = (int) (orgC.getBlue() * inverse + newC.getBlue() * opaque);
		int alpha = (int) (orgC.getAlpha() * inverse + newC.getAlpha() * opaque);
		return new Color(red, green, blue, alpha);
	}

	private static int colorDifference(Color a, Color b) {
		int red = Math.abs(a.getRed() - b.getRed());
		int green = Math.abs(a.getGreen() - b.getGreen());
		int blue = Math.abs(a.getBlue() - b.getBlue());
		int alpha = Math.abs(a.getAlpha() - b.getAlpha());
		return red + green + blue + alpha;
	}

	private static int calcIndex(int x, int y, int width) {
		return y * width + x;
	}
}
