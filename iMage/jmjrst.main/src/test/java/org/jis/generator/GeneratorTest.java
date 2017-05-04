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
		assertEquals(image, generator.rotateImage(image, 0.0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void illegalRotationTest() {
		generator.rotateImage(image, 0.7);
	}

}
