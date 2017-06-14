package org.iMage.geometrify;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * The {@link TrianglePictureFilter} is a {@link IPrimitiveFilter} which is able
 * to reconstruct an image through {@link Triangle}s.
 *
 * @author Tobias Hey
 *
 */
public class TrianglePictureFilter extends AbstractPrimitivePictureFilter {

	private static final int HEX_FF = 0xff;

	/**
	 * Constructs a {@link TrianglePictureFilter} with an specified
	 * {@link IPointGenerator}.
	 *
	 * @param pointGenerator
	 *            The {@link IPointGenerator} to use for generating
	 *            {@link Triangle} {@link Point}s
	 */
	public TrianglePictureFilter(IPointGenerator pointGenerator) {
		super(pointGenerator);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.iMage.geometrify.AbstractPrimitivePictureFilter#calculateColor(java.
	 * awt.image.BufferedImage, org.iMage.geometrify.IPrimitive)
	 */
	@Override
	protected Color calculateColor(BufferedImage original, IPrimitive primitive) {
		int bandwidth = (original.getColorModel().hasAlpha()) ? 4 : 3;

		int width = original.getWidth();
		int height = original.getHeight();

		Point start = primitive.getBoundingBox().getUpperLeftCorner();
		Point end = primitive.getBoundingBox().getLowerRightCorner();

		int numPixels = 0;

		int red = 0;
		int green = 0;
		int blue = 0;
		int alpha = 0;

		int wStart = Math.max(0, start.x);
		int wEnd = Math.min(width - 1, end.x);
		int hStart = Math.max(0, start.y);
		int hEnd = Math.min(height - 1, end.y);
		int[] imgPixels = new int[width * height * bandwidth];

		original.getRaster().getPixels(0, 0, width, height, imgPixels);

		Point currPixel = new Point();

		for (int y = hStart; y <= hEnd; y++) {
			for (int x = wStart; x <= wEnd; x++) {
				currPixel.setLocation(x, y);
				if (primitive.isInsidePrimitive(currPixel)) {
					int pixelIndex = (y * width + x) * bandwidth;
					numPixels++;

					if (bandwidth == 4) {
						alpha += imgPixels[pixelIndex + 3];

					}
					red += imgPixels[pixelIndex + 2];
					green += imgPixels[pixelIndex + 1];
					blue += imgPixels[pixelIndex];

				}
			}
		}

		// normalize color values
		if (numPixels > 0) {
			alpha = alpha / numPixels;
			red = red / numPixels;
			green = green / numPixels;
			blue = blue / numPixels;
		}
		if (original.getColorModel().hasAlpha()) {
			return new Color(red, green, blue, alpha);
		} else {
			return new Color(red, green, blue);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.iMage.geometrify.AbstractPrimitivePictureFilter#apply(java.awt.image.
	 * BufferedImage, int, int)
	 */
	@Override
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples) {
		int width = image.getWidth();
		int height = image.getHeight();

		// construct "empty" image
		BufferedImage result = new BufferedImage(width, height, image.getType());

		for (int i = 0; i < numberOfIterations; i++) {
			int bestDifference = Integer.MAX_VALUE;
			IPrimitive bestPrimitive = null;

			for (int s = 0; s < numberOfSamples; s++) {
				IPrimitive sample = generatePrimitive();
				sample.setColor(calculateColor(image, sample));
				int difference = calculateDifference(image, result, sample);

				if (difference <= bestDifference) {
					bestDifference = difference;
					bestPrimitive = sample;
				}
			}

			addToImage(result, bestPrimitive);
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.iMage.geometrify.AbstractPrimitivePictureFilter#generatePrimitive()
	 */
	@Override
	protected IPrimitive generatePrimitive() {
		return new Triangle(pointGenerator.nextPoint(), pointGenerator.nextPoint(), pointGenerator.nextPoint());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.iMage.geometrify.AbstractPrimitivePictureFilter#calculateDifference(
	 * java.awt.image.BufferedImage, java.awt.image.BufferedImage,
	 * org.iMage.geometrify.IPrimitive)
	 */
	@Override
	protected int calculateDifference(BufferedImage original, BufferedImage current, IPrimitive primitive) {
		assert (primitive.getColor() != null) : "Average color of primitive has not been calculated, yet";

		int bandwidth = (original.getColorModel().hasAlpha()) ? 4 : 3;

		int width = original.getWidth();
		int height = original.getHeight();

		int[] orgPixels = new int[width * height * bandwidth];
		int[] currPixels = new int[width * height * bandwidth];

		current.getRaster().getPixels(0, 0, width, height, currPixels);
		original.getRaster().getPixels(0, 0, width, height, orgPixels);

		Point currPixel = new Point();

		int difference = 0;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int pixelIndex = (y * width + x) * bandwidth;
				currPixel.setLocation(x, y);

				// inside Primitive use calculated avg between current and
				// primitive color as value to calculate difference with
				if (primitive.isInsidePrimitive(currPixel)) {

					int argb = primitive.getColor().getRGB();

					int red = (argb >> 16) & HEX_FF;
					int green = (argb >> 8) & HEX_FF;
					int blue = argb & HEX_FF;

					difference += Math.abs(orgPixels[pixelIndex] - ((currPixels[pixelIndex] + blue) / 2));
					difference += Math.abs(orgPixels[pixelIndex + 1] - ((currPixels[pixelIndex + 1] + green) / 2));
					difference += Math.abs(orgPixels[pixelIndex + 2] - ((currPixels[pixelIndex + 2] + red) / 2));

					if (bandwidth == 4) {
						int alpha = (argb >> 24) & HEX_FF;
						difference += Math.abs(orgPixels[pixelIndex + 3] - ((currPixels[pixelIndex + 3] + alpha) / 2));
					}
				} else {
					for (int n = 0; n < bandwidth; n++) {
						difference += Math.abs(orgPixels[pixelIndex + n] - currPixels[pixelIndex + n]);
					}
				}

				if (difference < 0) {
					return Integer.MAX_VALUE;
				}
			}
		}

		return difference;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.iMage.geometrify.AbstractPrimitivePictureFilter#addToImage(java.awt.
	 * image.BufferedImage, org.iMage.geometrify.IPrimitive)
	 */
	@Override
	protected void addToImage(BufferedImage current, IPrimitive primitive) {
		assert (primitive.getColor() != null) : "Average color of primitive has not been calculated, yet";

		int bandwidth = (current.getColorModel().hasAlpha()) ? 4 : 3;

		int width = current.getWidth();
		int height = current.getHeight();

		Point start = primitive.getBoundingBox().getUpperLeftCorner();
		Point end = primitive.getBoundingBox().getLowerRightCorner();

		int wStart = Math.max(0, start.x);
		int wEnd = Math.min(width - 1, end.x);
		int hStart = Math.max(0, start.y);
		int hEnd = Math.min(height - 1, end.y);

		int[] imgPixels = new int[width * height * bandwidth];

		current.getRaster().getPixels(0, 0, width, height, imgPixels);

		Point currPixel = new Point();

		for (int y = hStart; y <= hEnd; y++) {
			for (int x = wStart; x <= wEnd; x++) {
				currPixel.setLocation(x, y);
				if (primitive.isInsidePrimitive(currPixel)) {
					int pixelIndex = (y * width + x) * bandwidth;

					int argb = primitive.getColor().getRGB();

					int red = (argb >> 16) & HEX_FF;
					int green = (argb >> 8) & HEX_FF;
					int blue = argb & HEX_FF;

					imgPixels[pixelIndex] = Math.max(0, Math.min(255, ((blue + imgPixels[pixelIndex]) / 2)));
					imgPixels[pixelIndex + 1] = Math.max(0, Math.min(255, ((green + imgPixels[pixelIndex + 1]) / 2)));
					imgPixels[pixelIndex + 2] = Math.max(0, Math.min(255, ((red + imgPixels[pixelIndex + 2]) / 2)));

					if (bandwidth == 4) {
						int alpha = (argb >> 24) & HEX_FF;
						imgPixels[pixelIndex + 3] = Math.max(0, Math.min(255, ((alpha + imgPixels[pixelIndex + 3]) / 2)));
					}
				}
			}
		}
		current.getRaster().setPixels(0, 0, width, height, imgPixels);
	}
}
