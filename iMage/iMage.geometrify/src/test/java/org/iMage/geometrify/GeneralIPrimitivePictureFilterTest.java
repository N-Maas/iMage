package org.iMage.geometrify;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
	private static final String addition = "_test";
	private static final File TARGET_DIRECTORY = new File("./target/data_test/");
	private static final File TEST_NO_ALPHA = new File("./src/test/resources/no_alpha.png");
	private static final File TEST_ALPHA = new File("./src/test/resources/alpha.png");
	private static final File TEST_COLOR = new File("./src/test/resources/color.png");
	private static final File LANDSCAPE = new File("./src/test/resources/landscape.png");
	private static final File GENERATE_NO_ALPHA = new File("./target/data_test/generate_no_alpha" + addition + ".png");
	private static final File GENERATE_ALPHA = new File("./target/data_test/generate_alpha" + addition + ".png");
	private static final File GENERATE_LANDSCAPE = new File(
			"./target/data_test/generate_landscape" + addition + ".png");
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

	// /**
	// * Tests calculateColor() by applying it to a square-colored test picture.
	// */
	// @Test
	// public void calculateColorTest() {
	// int[][] data = new
	// int[this.imageColor.getWidth()][this.imageColor.getHeight()];
	// for (int i = 0; i < this.imageColor.getWidth(); i++) {
	// for (int j = 0; j < this.imageColor.getHeight(); j++) {
	// data[i][j] = this.imageColor.getRGB(i, j);
	// }
	// }
	//
	// assertEquals(Color.RED, new
	// Color(GeneralPrimitivePictureFilter.calculateColor(data,
	// new IPolygon(new Point(0, 0), new Point(20, 2), new Point(2, 20)))));
	// assertEquals(Color.WHITE, new
	// Color(GeneralPrimitivePictureFilter.calculateColor(data,
	// new IPolygon(new Point(30, 30), new Point(45, 30), new Point(30, 45)))));
	// }

	// @Test
	// public void colorAverageTest() {
	// for (int i = 0; i < 100; i++) {
	// Color c1 = new Color((int) (Math.random() * 256), (int) (Math.random() *
	// 256), (int) (Math.random() * 256),
	// (int) (Math.random() * 256));
	// Color c2 = new Color((int) (Math.random() * 256), (int) (Math.random() *
	// 256), (int) (Math.random() * 256),
	// (int) (Math.random() * 256));
	// assertEquals(
	// "R1: " + c1.getRed() + " R2: " + c2.getRed() + " - G1: " + c1.getGreen()
	// + " G2: " + c2.getGreen()
	// + " - B1: " + c1.getBlue() + " B2: " + c2.getBlue(),
	// new Color((c1.getRed() + c2.getRed()) / 2, (c1.getGreen() +
	// c2.getGreen()) / 2,
	// (c1.getBlue() + c2.getBlue()) / 2, (c1.getAlpha() + c2.getAlpha()) / 2),
	// new Color(GeneralPrimitivePictureFilter.colorAverage(c1.getRGB(),
	// c2.getRGB(), 0.5f), true));
	// }
	// }

	// @Test
	// public void colorDifferenceTest() {
	// for (int i = 0; i < 100; i++) {
	// Color c1 = new Color((int) (Math.random() * 256), (int) (Math.random() *
	// 256), (int) (Math.random() * 256),
	// (int) (Math.random() * 256));
	// Color c2 = new Color((int) (Math.random() * 256), (int) (Math.random() *
	// 256), (int) (Math.random() * 256),
	// (int) (Math.random() * 256));
	// assertEquals(
	// "R1: " + c1.getRed() + " R2: " + c2.getRed() + " - G1: " + c1.getGreen()
	// + " G2: " + c2.getGreen()
	// + " - B1: " + c1.getBlue() + " B2: " + c2.getBlue(),
	// GeneralPrimitivePictureFilter.colorDifference(c1, c2),
	// GeneralPrimitivePictureFilter.colorDifference(c1.getRGB(), c2.getRGB()));
	// }
	// }

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
		IPrimitiveGenerator gen = new RectangleGenerator(this.imageNoAlpha.getWidth(), this.imageNoAlpha.getHeight());
		// gen.setBounds(PolygonGenerator.RANDOM_BOUNDS);
		GeneralPrimitivePictureFilter filter = new GeneralPrimitivePictureFilter(gen, 0.45f);
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
		IPrimitiveGenerator gen = new RectangleGenerator(this.imageNoAlpha.getWidth(), this.imageNoAlpha.getHeight());
		// gen.setBounds(PolygonGenerator.RANDOM_BOUNDS);
		GeneralPrimitivePictureFilter filter = new GeneralPrimitivePictureFilter(gen, 0.45f);
		BufferedImage image = filter.apply(this.imageAlpha, 800, 50);
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
				new PolygonGenerator(this.imageLandscape.getWidth(), this.imageLandscape.getHeight(), 4, 8));
		BufferedImage image = filter.apply(this.imageLandscape, 1600, 100);
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
