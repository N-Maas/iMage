package gui;

import static org.junit.Assert.assertEquals;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;;

/**
 * Tests the Illustrate class.
 * 
 * @author Nikolai
 */
public class IllustrateTest {
	private static final File DEFAULT_IMG_PATH = new File("src/main/resources/Default.png");
	private BufferedImage defaultImage;

	/**
	 * Provides the default image for testing.
	 * 
	 * @throws IOException
	 *             if not finding the image
	 */
	@Before
	@Test
	public void setUp() throws IOException {
		this.defaultImage = ImageIO.read(DEFAULT_IMG_PATH);
	}

	/**
	 * Opens the gui for manual testing.
	 * 
	 * @throws InterruptedException
	 *             if interrupted
	 */
	@Ignore
	@Test
	public void guiTest() throws InterruptedException {
		SwingUtilities.invokeLater(() -> {
			Illustrate il = new Illustrate();
			il.getFrame().addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					synchronized (IllustrateTest.this) {
						IllustrateTest.this.notifyAll();
					}
				}
			});
			il.getFrame().setLocationRelativeTo(null);
			il.getFrame().setVisible(true);
		});
		synchronized (this) {
			this.wait();
		}
	}

	/**
	 * Tests the static image scale method of Illustrate.
	 */
	@Test
	public void scaleTest() {
		BufferedImage sclImage = Illustrate.scaleToBorders(this.defaultImage, 200, 200);
		assertEquals("Width", 195, sclImage.getWidth());
		assertEquals("Height", 200, sclImage.getHeight());
	}
}
