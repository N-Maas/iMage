package gui;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.iMage.geometrify.TriangleGenerator;
import org.junit.Before;
import org.junit.Test;

import filter.ObservableTPFilter;

public class FrameProviderTest {
	private static final File DEFAULT_IMG_PATH = new File("src/main/resources/Default.png");
	private BufferedImage defaultImage;
	private BufferedImage smallImage;

	@Before
	public void setUp() throws Exception {
		this.defaultImage = ImageIO.read(DEFAULT_IMG_PATH);
		this.smallImage = Illustrate.scaleToBorders(this.defaultImage, 100, 100);
	}

	@Test
	public void basicTest() {
		int[] counter = { 0 };
		int width = this.smallImage.getWidth(), height = this.smallImage.getHeight();
		ObservableTPFilter filter = new ObservableTPFilter(new TriangleGenerator(width, height));
		FrameProvider ip = new FrameProvider(filter, null);
		ip.addChangeListener(e -> counter[0]++);
		BufferedImage expected = filter.apply(this.smallImage, 50, 10);
		assertEquals(50, ip.getSize());
		assertEquals(50, ip.getCurrentIndex());
		this.assertImageEquals(expected, ip.getCurrentFrame());
		ip.setFrame(20);
		assertEquals(50, ip.getSize());
		assertEquals(20, ip.getCurrentIndex());
		ip.setFrame(50);
		assertEquals(50, ip.getSize());
		assertEquals(50, ip.getCurrentIndex());
		this.assertImageEquals(expected, ip.getCurrentFrame());
		assertEquals(52, counter[0]);
		ip.setFrame(0);
		this.assertImageEquals(new BufferedImage(width, height, this.smallImage.getType()), ip.getCurrentFrame());
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
