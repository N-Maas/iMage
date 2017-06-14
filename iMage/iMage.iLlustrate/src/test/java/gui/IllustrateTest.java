package gui;

import static org.junit.Assert.assertEquals;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;;

public class IllustrateTest {
	private static final File DEFAULT_IMG_PATH = new File("src/main/resources/Default.png");
	private BufferedImage defaultImage;

	@Before
	@Test
	public void setUp() throws IOException {
		this.defaultImage = ImageIO.read(DEFAULT_IMG_PATH);
	}

	@Test
	public void guiTest() throws InterruptedException {
		Illustrate il = new Illustrate();
		il.getFrame().setVisible(true);
		il.getFrame().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				synchronized (IllustrateTest.this) {
					IllustrateTest.this.notifyAll();
				}
			}
		});
		synchronized (this) {
			this.wait();
		}
	}

	@Test
	public void scaleTest() {
		BufferedImage sclImage = Illustrate.scaleToBorders(this.defaultImage, 200, 200);
		assertEquals("Width", 195, sclImage.getWidth());
		assertEquals("Height", 200, sclImage.getHeight());
	}
}
