package org.iMage.geometrify;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.iMage.geometrify.generators.PrimitiveGenerator;
import org.iMage.geometrify.primitives.ColoredPrimitive;
import org.iMage.geometrify.primitives.Primitive;

/**
 * Modifies an image by iteratively reconstructing it with triangles.
 *
 * @author Nikolai
 */
public class GeneralPrimitivePictureFilter implements PrimitivePictureFilter {
	private static final int BLUE = 255, GREEN = 255 << 8, RED = 255 << 16, ALPHA = 255 << 24;
	private static final int X_VAL = BLUE | GREEN, Y_VAL = RED | ALPHA;
	private final PrimitiveGenerator generator;
	private final float opaque;

	/**
	 * Constructs the filter within the specified point generator.
	 *
	 * @param pointGenerator
	 *            the pointGenerator
	 */
	public GeneralPrimitivePictureFilter(PrimitiveGenerator generator) {
		this(generator, 0.5f);
	}

	/**
	 * Constructs the filter within the specified point generator.
	 *
	 * @param pointGenerator
	 *            the pointGenerator
	 */
	public GeneralPrimitivePictureFilter(PrimitiveGenerator generator, float opaque) {
		if (opaque <= 0 || opaque > 1) {
			throw new IllegalArgumentException("Opaque out of bounds: (0, 1]");
		}
		this.generator = generator;
		this.opaque = opaque;
	}

	public static int calculateColor(int[][] data, Primitive primitive) {
		int[] insidePoints = primitive.getInsidePoints();
		long blue = 0;
		long green = 0;
		long red = 0;
		long alpha = 0;
		if (insidePoints.length == 0) {
			return 0;
		}

		for (int i = 0; i < insidePoints.length; i += 2) {
			long val = Integer.toUnsignedLong(data[insidePoints[i]][insidePoints[i + 1]]);
			blue += val & BLUE;
			green += val & GREEN;
			red += val & RED;
			alpha += val & ALPHA;
		}
		int length = insidePoints.length / 2;
		int result = (int) (blue / length) | ((int) (green / length) & GREEN) | ((int) (red / length) & RED)
				| (((int) (alpha / length)) & ALPHA);
		// System.out.println(" r:" + (red / insidePoints.length) + " g:" +
		// (green / insidePoints.length) + "b :"
		// + (blue / insidePoints.length));
		// System.out.println("RET " + new Color(result));
		return result;

	}

	@Override
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples) {
		if (numberOfIterations < 1 || numberOfSamples < 1) {
			throw new IllegalArgumentException("Numbers must be greater 0.");
		}

		// reuseable resources
		ExecutorService executor = Executors.newFixedThreadPool(numberOfSamples);
		Primitive[] prims = new Primitive[numberOfSamples];
		int[] colors = new int[numberOfSamples];
		int[] diffs = new int[numberOfSamples];
		int[][] orgSample = GeneralPrimitivePictureFilter.toData(image);
		int[][] newSample = new int[image.getWidth()][image.getHeight()];
		boolean hasAlpha = image.getColorModel().hasAlpha();

		for (int i = 0; i < numberOfIterations; i++) {
			CountDownLatch latch = new CountDownLatch(numberOfSamples);
			for (int j = 0; j < numberOfSamples; j++) {
				if (Thread.interrupted()) {
					return GeneralPrimitivePictureFilter.toImage(newSample, image);
				}
				final int puffer = j;
				executor.submit(() -> {
					try {
						prims[puffer] = this.generator.generatePrimitive();
						colors[puffer] = GeneralPrimitivePictureFilter.calculateColor(orgSample, prims[puffer]);
						diffs[puffer] = this.calculateDifference(orgSample, newSample,
								prims[puffer].ofColor(colors[puffer]));
						latch.countDown();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
			try {
				latch.await();
			} catch (InterruptedException e) {
				return GeneralPrimitivePictureFilter.toImage(newSample, image);
			}

			int min = Integer.MAX_VALUE;
			int minIndex = 0;
			for (int k = 0; k < numberOfSamples; k++) {
				if (diffs[k] <= min) {
					min = diffs[k];
					minIndex = k;
				}
			}

			ColoredPrimitive prim = prims[minIndex].ofColor(colors[minIndex]);
			this.addToImage(newSample, prim);
			this.processIteration(newSample, prim);
		}
		return GeneralPrimitivePictureFilter.toImage(newSample, image);
	}

	public void addToImage(BufferedImage current, ColoredPrimitive primitive) {
		int[] insidePoints = primitive.getInsidePoints();
		int[][] orgSample = new int[current.getWidth()][current.getHeight()];
		for (int i = 0; i < insidePoints.length; i += 2) {
			int x = insidePoints[i], y = insidePoints[i + 1];
			orgSample[x][y] = current.getRGB(x, y);
		}
		this.addToImage(orgSample, primitive);
		for (int i = 0; i < insidePoints.length; i += 2) {
			int x = insidePoints[i], y = insidePoints[i + 1];
			current.setRGB(x, y, orgSample[x][y]);
		}

	}

	protected void processIteration(int[][] newData, ColoredPrimitive primitive) {
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

	public int calculateDifference(int[][] orgData, int[][] newData, ColoredPrimitive primitive) {
		int[] insidePoints = primitive.getInsidePoints();
		int rgb = primitive.getColor().getRGB();
		int difference = 0;
		for (int i = 0; i < insidePoints.length; i += 2) {
			int x = insidePoints[i], y = insidePoints[i + 1];
			int orgColor = orgData[x][y];
			int oldColor = newData[x][y];
			int newColor = colorAverage(oldColor, rgb, this.opaque);
			difference += colorDifference(orgColor, newColor) - colorDifference(orgColor, oldColor);
		}
		return difference;
	}

	private void addToImage(int[][] data, ColoredPrimitive primitive) {
		int[] insidePoints = primitive.getInsidePoints();
		int rgb = primitive.getColor().getRGB();
		for (int i = 0; i < insidePoints.length; i += 2) {
			int x = insidePoints[i], y = insidePoints[i + 1];
			data[x][y] = colorAverage(data[x][y], rgb, this.opaque);
		}
	}

	// private static Color colorAverage(Color orgC, Color newC, float opaque) {
	// float inverse = 1 - opaque;
	// int red = (int) (orgC.getRed() * inverse + newC.getRed() * opaque);
	// int green = (int) (orgC.getGreen() * inverse + newC.getGreen() * opaque);
	// int blue = (int) (orgC.getBlue() * inverse + newC.getBlue() * opaque);
	// int alpha = (int) (orgC.getAlpha() * inverse + newC.getAlpha() * opaque);
	// return new Color(red, green, blue, alpha);
	// }

	private static int colorAverage(int orgC, int newC, float opaque) {
		float inverse = 1 - opaque;
		int blue = (int) ((orgC & BLUE) * inverse + (newC & BLUE) * opaque);
		int green = (int) ((orgC & GREEN) * inverse + (newC & GREEN) * opaque) & GREEN;
		int red = (int) ((orgC & RED) * inverse + (newC & RED) * opaque) & RED;
		int alpha = (int) (Integer.toUnsignedLong((orgC & ALPHA)) * inverse)
				+ (int) (Integer.toUnsignedLong((newC & ALPHA)) * opaque) & ALPHA;
		return blue | green | red | alpha;

	}

	// public static int colorDifference(Color a, Color b) {
	// int red = Math.abs(a.getRed() - b.getRed());
	// int green = Math.abs(a.getGreen() - b.getGreen());
	// int blue = Math.abs(a.getBlue() - b.getBlue());
	// int alpha = Math.abs(a.getAlpha() - b.getAlpha());
	// return red + green + blue + alpha;
	// }

	private static int colorDifference(int a, int b) {
		int blue = Math.abs((a & BLUE) - (b & BLUE));
		int green = Math.abs((a & GREEN) - (b & GREEN)) >>> 8;
		int red = Math.abs((a & RED) - (b & RED)) >>> 16;
		int alpha = (int) (Math.abs(Integer.toUnsignedLong((a & ALPHA)) - Integer.toUnsignedLong((b & ALPHA))) >>> 24);
		return blue + green + red + alpha;
	}

	private static int[][] toData(BufferedImage image) {
		int[][] data = new int[image.getWidth()][image.getHeight()];
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				data[i][j] = image.getRGB(i, j);
			}
		}
		return data;
	}

	private static BufferedImage toImage(int[][] data, BufferedImage original) {
		BufferedImage newImage = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {
				newImage.setRGB(i, j, data[i][j]);
			}
		}
		return newImage;
	}
}
