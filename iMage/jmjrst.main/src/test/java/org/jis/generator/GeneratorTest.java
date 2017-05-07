package org.jis.generator;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {
	private Generator generator;
	private BufferedImage image;
	private BufferedImage generatedImage;

	@Before
	public void setUp() {
		generator = new Generator(null, 0);
		ImageInputStream iis;
		try {
			File picture = new File(".\\src\\test\\resources\\picture.jpg");
			assertTrue(picture.exists());
			iis = ImageIO.createImageInputStream(picture);
			ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
			reader.setInput(iis, true);
			ImageReadParam params = reader.getDefaultReadParam();
			image = reader.read(0, params);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException reading picture.");
		}
	}

	@Test
	public void basicRotationTest() {
		assertNull(generator.rotateImage(null, 0.0));
		BufferedImage test = generator.rotateImage(image, 0.0);
		assertEquals(image, test);
		
		generatedImage = test;
	}

	@Test(expected=IllegalArgumentException.class)
	public void illegalRotationTest() {
		generator.rotateImage(image, 0.7);
	}

	@Test
	public void rotate90Test() {
		BufferedImage rotate90 = generator.rotateImage(image, Math.toRadians(90));
		assertEquals(image.getWidth(), rotate90.getHeight());
		assertEquals(image.getHeight(), rotate90.getWidth());
		
		// testing whether it is the same (rotated) image
		for (int i = 0; i < image.getWidth(); i += 10) {
			for (int j = 0; j < image.getHeight(); j += 10) {
				assertEquals(image.getRGB(i, j), rotate90.getRGB(image.getHeight() - 1 - j, i));
			}
		}
		
		generatedImage = rotate90;
	}
	
	@Test
	public void rotate270Test() {
		BufferedImage rotate270 = generator.rotateImage(image, Math.toRadians(270));
		assertEquals(image.getWidth(), rotate270.getHeight());
		assertEquals(image.getHeight(), rotate270.getWidth());
		
		// testing whether it is the same (rotated) image
		for (int i = 0; i < image.getWidth(); i += 10) {
			for (int j = 0; j < image.getHeight(); j += 10) {
				assertEquals(image.getRGB(i, j), rotate270.getRGB(j, image.getWidth() - 1 - i));
			}
		}
		
		generatedImage = rotate270;
	}

	@Test
	public void rotate180Test() {
		BufferedImage rotate180 = generator.rotateImage(image, Math.toRadians(180));
		assertEquals(image.getWidth(), rotate180.getWidth());
		assertEquals(image.getHeight(), rotate180.getHeight());
		
		// testing whether it is the same (rotated) image
		for (int i = 0; i < image.getWidth(); i += 10) {
			for (int j = 0; j < image.getHeight(); j += 10) {
				assertEquals(image.getRGB(i, j), rotate180.getRGB(image.getWidth() - 1 - i, image.getHeight() - 1 - j));
			}
		}
		
		generatedImage = rotate180;
	}

}
