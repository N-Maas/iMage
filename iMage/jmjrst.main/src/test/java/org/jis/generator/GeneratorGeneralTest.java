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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GeneratorGeneralTest {
	private static final File DIRECTORY = new File(".\\target\\data_test\\");
	private Generator generator;
	private BufferedImage image;
	private File path;

	private BufferedImage readImage(File file) {
		try {
			assertTrue(file.exists());
			ImageInputStream iis = ImageIO.createImageInputStream(file);
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
		return image;
	}

	private void writeToFile() {
		FileOutputStream fos;
		ImageWriter writer;
		ImageOutputStream ios;
		try {
			path = new File(".\\target\\data_test\\testPicture_" + new SimpleDateFormat("HHmmss_SSS").format(new Date())
					+ ".jpg");
			assertTrue("Generated picture already exists or could not be created.", path.createNewFile());

			fos = new FileOutputStream(path);
			writer = ImageIO.getImageWritersByFormatName("jpg").next();
			ios = ImageIO.createImageOutputStream(fos);
			writer.setOutput(ios);

			writer.write(new IIOImage(image, null, null));

			ios.flush();
			writer.dispose();
			ios.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a generator and a generic test image.
	 */
	@Before
	public void setUp() {
		generator = new Generator(null, 0);
		image = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				image.setRGB(i, j, (255 - i / 2) + ((255 - j * 2 / 3) << 8) + (255 << 24));
			}
		}
	}

	/**
	 * Tests rotating with a null argument and rotating by 0°.
	 */
	@Test
	public void rotationTest() {
		assertNull(generator.rotateImage(null, 0.0));
		assertEquals(image, generator.rotateImage(image, 0.0));

		BufferedImage rotate180 = generator.rotateImage(image, Math.toRadians(180));
		assertEquals(image.getWidth(), rotate180.getWidth());
		assertEquals(image.getHeight(), rotate180.getHeight());

		// testing whether it is the same (rotated) image
		for (int i = 0; i < image.getWidth(); i += 10) {
			for (int j = 0; j < image.getHeight(); j += 10) {
				assertEquals(image.getRGB(i, j), rotate180.getRGB(image.getWidth() - 1 - i, image.getHeight() - 1 - j));
			}
		}

		BufferedImage rotate90 = generator.rotateImage(image, Math.toRadians(90));
		assertEquals(image.getWidth(), rotate90.getHeight());
		assertEquals(image.getHeight(), rotate90.getWidth());

		// testing whether it is the same (rotated) image
		for (int i = 0; i < image.getWidth(); i += 10) {
			for (int j = 0; j < image.getHeight(); j += 10) {
				assertEquals(image.getRGB(i, j), rotate90.getRGB(image.getHeight() - 1 - j, i));
			}
		}
	}

	@Test
	public void generateImageTest() {
		this.writeToFile();
		try {
			generator.generateImage(path, DIRECTORY, false, 800, 600, "generated_");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Unexpected Exception.");
		}
		BufferedImage testImage = this.readImage(new File(DIRECTORY, "generated_" + path.getName()));
		assertEquals(800, testImage.getWidth());
		assertEquals(600, testImage.getHeight());
	}
	
	@Ignore
	@Test
	public void fileRotationTest(){
		this.writeToFile();
		generator.rotate(path);
		BufferedImage testImage = this.readImage(path);
		assertEquals(300, testImage.getWidth());
		assertEquals(400, testImage.getHeight());
	}
}
