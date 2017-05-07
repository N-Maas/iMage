package org.jis.generator;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {
	private static File TARGET_DIRECTORY = new File(".\\target\\data_test\\");
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

			iis.close();
			reader.dispose();
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException reading picture.");
		}
	}

	@After
	public void tearDown() {
		// null can not be saved
		if(generatedImage == null) {
			return;
		}
		// creates the directory, if not existing
		if(!TARGET_DIRECTORY.exists()) {
			assertTrue("Could not create " + TARGET_DIRECTORY, TARGET_DIRECTORY.mkdirs());
		}
		
		try {
			File pictureGen = new File(".\\target\\data_test\\rotatedPicture_"
					+ new SimpleDateFormat("HHmmss_SSS").format(new Date()) + ".jpg");
			assertTrue("Generated picture already exists or could not be created.", pictureGen.createNewFile());

			FileOutputStream fos = new FileOutputStream(pictureGen);
			ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
			ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
			writer.setOutput(ios);

			writer.write(new IIOImage(generatedImage, null, null));
			ios.flush();
			writer.dispose();
			ios.close();
			fos.close();
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

	@Test(expected = IllegalArgumentException.class)
	public void illegalRotationTest() {
		// avoids saving any image
		generatedImage = null;
		
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
