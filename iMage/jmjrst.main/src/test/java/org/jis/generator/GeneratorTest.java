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
			System.out.println(picture.getPath());
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
	public void test() {
	}

}