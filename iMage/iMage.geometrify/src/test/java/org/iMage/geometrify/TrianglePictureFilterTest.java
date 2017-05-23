package org.iMage.geometrify;

import static org.junit.Assert.assertEquals;
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
import org.junit.Test;

public class TrianglePictureFilterTest {
	private static final File TARGET_DIRECTORY = new File("./target/data_test/");
	private static final File TEST_NO_ALPHA = new File("./src/test/resources/no_alpha.png");
	private static final File TEST_ALPHA = new File("./src/test/resources/alpha.png");
	private static final File TEST_COLOR = new File("./src/test/resources/color.png");
	private BufferedImage imageNoAlpha;
	private BufferedImage imageAlpha;
	private BufferedImage imageColor;
	private BufferedImage generatedImage;

	@BeforeClass
	public static void setUpBeforeClass() {
		// creates the directory, if not existing
		if (!TARGET_DIRECTORY.exists()) {
			assertTrue("Could not create " + TARGET_DIRECTORY, TARGET_DIRECTORY.mkdirs());
		}
		assertTrue(TEST_NO_ALPHA.exists());
		assertTrue(TEST_ALPHA.exists());
		assertTrue(TEST_COLOR.exists());
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
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException reading picture.");
		}
	}

	@Test
	public void calculateColorTest() {
		TrianglePictureFilter filter = new TrianglePictureFilter(new RandomPointGenerator(0, 0));
		assertEquals(Color.RED, filter.calculateColor(this.imageColor,
				new Triangle(new Point(0, 0), new Point(20, 2), new Point(2, 20))));
		assertEquals(Color.WHITE, filter.calculateColor(this.imageColor,
				new Triangle(new Point(30, 30), new Point(45, 30), new Point(30, 45))));
		// assertEquals(new Color(0, 0, 255, 127),
		// filter.calculateColor(this.imageColor,
		// new Triangle(new Point(2, 55), new Point(0, 66), new Point(22,
		// 61))));
		System.out.println(filter.calculateColor(this.imageColor,
				new Triangle(new Point(20, 20), new Point(30, 20), new Point(20, 30))));
		Color c = filter.calculateColor(this.imageAlpha,
				new Triangle(new Point(0, 0), new Point(0, 150), new Point(130, 150)));
		System.out.println(c + " a = " + c.getAlpha());
	}

}
