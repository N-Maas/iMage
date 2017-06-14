package org.iMage.geometrify;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class for {@link TrianglePictureFilter}.
 *
 * @author Dominic Ziegler, Tobias Hey
 * @version 1.0
 */
public class IPDTrianglePictureFilterTest {
	private Triangle triangleUpperLeft, triangleLowerLeft;
	private TrianglePictureFilter filter;

	/**
	 * Set up the test objects.
	 */
	@Before
	public void setUp() {
		Point a = new Point(0, 0);
		Point b = new Point(0, 1);
		Point c = new Point(1, 0);
		this.triangleUpperLeft = new IPDTriangle(a, b, c);
		c = new Point(1, 1);
		this.triangleLowerLeft = new IPDTriangle(a, b, c);
		this.filter = new TrianglePictureFilter(new IPDNonRandomPointGenerator(2, 2));
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#calculateColor(BufferedImage, IPrimitive)}
	 * without alpha channel.
	 */
	@Test
	public void testCalculateColor() {
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0, 0xFFFFFF);
		Color color = this.filter.calculateColor(img, this.triangleUpperLeft);
		assertEquals(0xFF555555, color.getRGB());
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#calculateColor(BufferedImage, IPrimitive)}
	 * with alpha channel.
	 */
	@Test
	public void testCalculateColorAlpha() {
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		img.setRGB(0, 0, 0xFFFFFFFF);
		Color color = this.filter.calculateColor(img, this.triangleUpperLeft);
		assertEquals(0x55555555, color.getRGB());
	}

	/**
	 * Complex test case for
	 * {@link TrianglePictureFilter#apply(BufferedImage, int, int)}.
	 *
	 * @throws IOException
	 *             if some I/O operation failed
	 */
	@Test
	public void testApply() throws IOException {
		final int numberOfIterations = 10;
		final int numberOfSamples = 10;

		BufferedImage walter = ImageIO.read(this.getClass().getResource("/walter_no_alpha.png"));
		this.filter = new TrianglePictureFilter(new IPDNonRandomPointGenerator(walter.getWidth(), walter.getHeight()));
		BufferedImage result = this.filter.apply(walter, numberOfIterations, numberOfSamples);
		BufferedImage expected = ImageIO
				.read(this.getClass().getResource("/walter_no_alpha_out_10iterations_10samples.png"));
		this.assertImageEquals(expected, result);
	}

	/**
	 * Complex test case for
	 * {@link TrianglePictureFilter#apply(BufferedImage, int, int)}.
	 *
	 * @throws IOException
	 *             if some I/O operation failed
	 */
	@Test
	@Ignore("Could result in long runtime")
	public void testApply42() throws IOException {
		final int numberOfIterations = 42;
		final int numberOfSamples = 42;

		BufferedImage walter = ImageIO.read(this.getClass().getResource("/walter_no_alpha.png"));
		this.filter = new TrianglePictureFilter(new IPDNonRandomPointGenerator(walter.getWidth(), walter.getHeight()));
		BufferedImage result = this.filter.apply(walter, numberOfIterations, numberOfSamples);
		BufferedImage expected = ImageIO
				.read(this.getClass().getResource("/walter_no_alpha_out_42iterations_42samples.png"));
		this.assertImageEquals(expected, result);
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#calculateDifference(BufferedImage, BufferedImage, IPrimitive)}
	 * without alpha channel.
	 */
	@Test
	public void testCalculateDifference() {
		BufferedImage original = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		BufferedImage current = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		current.setRGB(0, 0, 0x555555);
		this.triangleUpperLeft.setColor(Color.WHITE);
		assertEquals(1272, this.filter.calculateDifference(original, current, this.triangleUpperLeft));
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#calculateDifference(BufferedImage, BufferedImage, IPrimitive)}
	 * with overlap and without alpha channel.
	 */
	@Test
	public void testCalculateDifferenceOverlap() {
		// set original to white
		BufferedImage original = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		original.setRGB(0, 0, 0xFFFFFF);
		original.setRGB(0, 1, 0xFFFFFF);
		original.setRGB(1, 0, 0xFFFFFF);
		original.setRGB(1, 1, 0xFFFFFF);

		// simulate added upperleftTriangle in white
		BufferedImage current = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		current.setRGB(0, 0, 0x7F7F7F);
		current.setRGB(0, 1, 0x7F7F7F);
		current.setRGB(1, 0, 0x7F7F7F);

		this.triangleLowerLeft.setColor(Color.WHITE);
		assertEquals(1152, this.filter.calculateDifference(original, current, this.triangleLowerLeft));
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#calculateDifference(BufferedImage, BufferedImage, IPrimitive)}
	 * without alpha channel.
	 */
	@Test
	public void testCalculateDifferenceAlpha() {
		BufferedImage original = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		BufferedImage current = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		current.setRGB(0, 0, 0x55555555);
		this.triangleUpperLeft.setColor(Color.WHITE);
		assertEquals(1696, this.filter.calculateDifference(original, current, this.triangleUpperLeft));
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#calculateDifference(BufferedImage, BufferedImage, IPrimitive)}
	 * with overlap and alpha channel.
	 */
	@Test
	public void testCalculateDifferenceOverlapAlpha() {
		// set original to white
		BufferedImage original = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		original.setRGB(0, 0, 0xFFFFFFFF);
		original.setRGB(0, 1, 0xFFFFFFFF);
		original.setRGB(1, 0, 0xFFFFFFFF);
		original.setRGB(1, 1, 0xFFFFFFFF);

		// simulate added upperleftTriangle in white
		BufferedImage current = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		current.setRGB(0, 0, 0x7F7F7F7F);
		current.setRGB(0, 1, 0x7F7F7F7F);
		current.setRGB(1, 0, 0x7F7F7F7F);

		this.triangleLowerLeft.setColor(new Color(0xFFFFFFFF, true)); // transparent
																		// white
		assertEquals(1536, this.filter.calculateDifference(original, current, this.triangleLowerLeft));
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#addToImage(BufferedImage, IPrimitive)} with
	 * overlap and without alpha channel.
	 */
	@Test
	public void testAddToImageOverlap() {
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		this.triangleUpperLeft.setColor(Color.WHITE);
		this.triangleLowerLeft.setColor(Color.WHITE);
		this.filter.addToImage(img, this.triangleUpperLeft);
		this.filter.addToImage(img, this.triangleLowerLeft);

		BufferedImage expected = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		expected.setRGB(1, 1, 0x7F7F7F);
		expected.setRGB(1, 0, 0x7F7F7F);
		expected.setRGB(0, 1, 0xBFBFBF);
		expected.setRGB(0, 0, 0xBFBFBF);
		this.assertImageEquals(expected, img);
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#addToImage(BufferedImage, IPrimitive)}
	 * without alpha channel.
	 */
	@Test
	public void testAddToImage() {
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		this.triangleUpperLeft.setColor(Color.WHITE);
		this.filter.addToImage(img, this.triangleUpperLeft);

		BufferedImage expected = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
		expected.setRGB(0, 0, 0x7F7F7F);
		expected.setRGB(1, 0, 0x7F7F7F);
		expected.setRGB(0, 1, 0x7F7F7F);
		this.assertImageEquals(expected, img);
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#addToImage(BufferedImage, IPrimitive)} with
	 * alpha channel.
	 */
	@Test
	public void testAddToImageAlpha() {
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		this.triangleUpperLeft.setColor(new Color(0xFFFFFFFF, true)); // transparent
																		// white
		this.filter.addToImage(img, this.triangleUpperLeft);

		BufferedImage expected = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		expected.setRGB(0, 0, 0x7F7F7F7F);
		expected.setRGB(1, 0, 0x7F7F7F7F);
		expected.setRGB(0, 1, 0x7F7F7F7F);
		this.assertImageEquals(expected, img);
	}

	/**
	 * Test case for
	 * {@link TrianglePictureFilter#addToImage(BufferedImage, IPrimitive)} with
	 * overlap and alpha channel.
	 */
	@Test
	public void testAddToImageOverlapAlpha() {
		BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		this.triangleUpperLeft.setColor(new Color(0xFFFFFFFF, true)); // transparent
																		// white
		this.triangleLowerLeft.setColor(new Color(0xFFFFFFFF, true)); // transparent
																		// white
		this.filter.addToImage(img, this.triangleUpperLeft);
		this.filter.addToImage(img, this.triangleLowerLeft);

		BufferedImage expected = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
		expected.setRGB(1, 1, 0x7F7F7F7F);
		expected.setRGB(1, 0, 0x7F7F7F7F);
		expected.setRGB(0, 1, 0xBFBFBFBF);
		expected.setRGB(0, 0, 0xBFBFBFBF);
		this.assertImageEquals(expected, img);
	}

	private void assertImageEquals(BufferedImage expected, BufferedImage actual) {
		int height = actual.getHeight();
		int width = actual.getWidth();
		assertEquals(expected.getHeight(), height);
		assertEquals(expected.getWidth(), width);
		assertArrayEquals(expected.getRaster().getPixels(0, 0, width, height, (int[]) null),
				actual.getRaster().getPixels(0, 0, width, height, (int[]) null));
	}
}
