package org.iMage.geometrify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the GeneralIPrimitivePictureFilter. The tests of the apply method
 * generate new filtered pictures into target/data_test/. The expensive
 * landscapeTest is ignored by default.
 * 
 * @author Nikolai
 */
public class GeneralIPrimitivePictureFilterTest {
	private static final File TARGET_DIRECTORY = new File("./target/data_test/");
	private static final File TEST_NO_ALPHA = new File("./src/test/resources/no_alpha.png");
	private static final File TEST_ALPHA = new File("./src/test/resources/alpha.png");
	private static final File TEST_COLOR = new File("./src/test/resources/color.png");
	private static final File LANDSCAPE = new File("./src/test/resources/landscape.png");
	private static final File GENERATE_NO_ALPHA = new File("./target/data_test/generate_no_alpha.png");
	private static final File GENERATE_ALPHA = new File("./target/data_test/generate_alpha.png");
	private static final File GENERATE_LANDSCAPE = new File("./target/data_test/generate_landscape.png");
	private BufferedImage imageNoAlpha;
	private BufferedImage imageAlpha;
	private BufferedImage imageColor;
	private BufferedImage imageLandscape;

	/**
	 * Asserts that the target directory and the test images exist.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		// creates the directory, if not existing
		if (!TARGET_DIRECTORY.exists()) {
			assertTrue("Could not create " + TARGET_DIRECTORY, TARGET_DIRECTORY.mkdirs());
		}
		assertTrue(TEST_NO_ALPHA.exists());
		assertTrue(TEST_ALPHA.exists());
		assertTrue(TEST_COLOR.exists());
		assertTrue(LANDSCAPE.exists());
	}

	/**
	 * Reads the picture.jpg file and provides it for the test cases.
	 */
	@Before
	public void setUp() {
		try {
			this.imageNoAlpha = ImageIO.read(TEST_NO_ALPHA);
			this.imageAlpha = ImageIO.read(TEST_ALPHA);
			this.imageColor = ImageIO.read(TEST_COLOR);
			this.imageLandscape = ImageIO.read(LANDSCAPE);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException reading pictures.");
		}
	}

	/**
	 * Tests calculateColor() by applying it to a square-colored test picture.
	 */
	@Test
	public void calculateColorTest() {
		GeneralPrimitivePictureFilter filter = new GeneralPrimitivePictureFilter(new RectangleGenerator(2, 2));
		assertEquals(Color.RED, filter.calculateColor(this.imageColor,
				new IPolygon(new Point(0, 0), new Point(20, 2), new Point(2, 20))));
		assertEquals(Color.WHITE, filter.calculateColor(this.imageColor,
				new IPolygon(new Point(30, 30), new Point(45, 30), new Point(30, 45))));
		// assertEquals(new Color(0, 0, 255, 127),
		// filter.calculateColor(this.imageColor,
		// new Triangle(new Point(2, 55), new Point(0, 66), new Point(22,
		// 61))));

		assertEquals(new Color(141, 73, 73), filter.calculateColor(this.imageColor,
				new IPolygon(new Point(20, 20), new Point(30, 20), new Point(20, 30))));
		assertEquals(new Color(14, 10, 7, 20), filter.calculateColor(this.imageAlpha,
				new IPolygon(new Point(0, 0), new Point(0, 150), new Point(130, 150))));
	}

	/**
	 * Tests whether an exception is thrown for to small parameter values at
	 * apply().
	 */
	@Test(expected = IllegalArgumentException.class)
	public void applyZeroTest() {
		GeneralPrimitivePictureFilter filter = new GeneralPrimitivePictureFilter(new RectangleGenerator(2, 2));
		filter.apply(this.imageColor, 0, 0);
	}

	/**
	 * Applies the filter to a smaller version of the walter_no_alpha test
	 * picture, saves the result and tests the size and alpha.
	 */
	@Test
	public void applyNoAlphaTest() {
		GeneralPrimitivePictureFilter filter = new GeneralPrimitivePictureFilter(
				new PolygonGenerator(this.imageNoAlpha.getWidth(), this.imageNoAlpha.getHeight(), 7));
		BufferedImage image = filter.apply(this.imageNoAlpha, 500, 30);
		assertEquals(this.imageNoAlpha.getWidth(), image.getWidth());
		assertEquals(this.imageNoAlpha.getHeight(), image.getHeight());
		assertFalse(image.getColorModel().hasAlpha());
		try {
			ImageIO.write(image, "png", GENERATE_NO_ALPHA);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException.");
		}
	}

	/**
	 * Applies the filter to a smaller version of the dices_alpha test picture,
	 * saves the result and tests the size and alpha.
	 */
	@Test
	public void applyAlphaTest() {
		GeneralPrimitivePictureFilter filter = new GeneralPrimitivePictureFilter(
				new PolygonGenerator(this.imageAlpha.getWidth(), this.imageAlpha.getHeight(), 7));
		BufferedImage image = filter.apply(this.imageAlpha, 1000, 50);
		assertEquals(this.imageAlpha.getWidth(), image.getWidth());
		assertEquals(this.imageAlpha.getHeight(), image.getHeight());
		try {
			assertTrue(image.getColorModel().hasAlpha());
			ImageIO.write(image, "png", GENERATE_ALPHA);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException.");
		}
	}

	/**
	 * Applies the filter to a relatively big landscape picture, saves the
	 * result and tests the size.
	 */
	@Ignore
	@Test
	public void landscapeTest() {
		GeneralPrimitivePictureFilter filter = new GeneralPrimitivePictureFilter(
				new RectangleGenerator(this.imageLandscape.getWidth(), this.imageLandscape.getHeight()));
		BufferedImage image = filter.apply(this.imageLandscape, 2000, 40);
		assertEquals(this.imageLandscape.getWidth(), image.getWidth());
		assertEquals(this.imageLandscape.getHeight(), image.getHeight());
		try {
			ImageIO.write(image, "png", GENERATE_LANDSCAPE);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected IOException.");
		}
	}
}
