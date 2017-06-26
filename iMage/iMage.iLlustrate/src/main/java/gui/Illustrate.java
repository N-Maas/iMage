package gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.iMage.geometrify.PrimitiveGenerator;

import filter.ObservableTPFilter;

/**
 * Central class of iLlustrate that holds the gui elements of the main frame and
 * delegates the events.
 * 
 * @author Nikolai
 */
public class Illustrate {
	private static final String DEFAULT_IMG = "/Default.png";
	private static final int ITERATIONS_MAX = 2000;
	private static final int ITERATIONS_DEFAULT = 100;
	private static final int SAMPLES_MAX = 200;
	private static final int SAMPLES_DEFAULT = 30;
	private static final int PREVIEW_WIDTH = 150;
	private static final int PREVIEW_HEIGHT = 150;
	private static final int MAX_WIDTH = 1024;
	private static final int MAX_HEIGHT = 768;

	private final ExecutorService executor;
	private final JFrame frame;
	private final JLabel original;
	private final JLabel preview;
	private final LabeledSlider iterations;
	private final LabeledSlider samples;
	private final JFileChooser chooser;
	private final BiFunction<Integer, Integer, PrimitiveGenerator> genFunction;
	private BufferedImage currentImage;
	private String fileName = "Default.png";

	/**
	 * Constructor that creates the gui and all needed listeners.
	 */
	public Illustrate(BiFunction<Integer, Integer, PrimitiveGenerator> genFunction) {
		this.executor = Executors.newSingleThreadExecutor();
		this.frame = new JFrame("iLlustrate");

		this.original = new JLabel();
		this.original.setPreferredSize(new Dimension(150, 150));
		this.preview = new JLabel();
		this.preview.setPreferredSize(new Dimension(150, 150));
		this.iterations = new LabeledSlider("Iterations", ITERATIONS_MAX, ITERATIONS_DEFAULT, 250, 10);
		this.samples = new LabeledSlider("Samples", SAMPLES_MAX, SAMPLES_DEFAULT, 250, 10);
		this.chooser = new JFileChooser();
		this.chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
		this.chooser.setAcceptAllFileFilterUsed(false);
		this.genFunction = genFunction;

		JButton load = new JButton("Load");
		JButton run = new JButton("Run");

		this.setUpComponents(load, run);

		this.setUpLayout(load, run);
	}

	/**
	 * Returns the used frame.
	 * 
	 * @return frame holding the GUI
	 */
	public JFrame getFrame() {
		return this.frame;
	}

	/**
	 * Updates the current image and starts calculating the preview.
	 * 
	 * @param img
	 *            new image
	 */
	public final void setCurrentImage(BufferedImage img) {
		this.currentImage = img;
		BufferedImage preImg = scaleToBorders(img, PREVIEW_WIDTH, PREVIEW_HEIGHT);
		this.original.setIcon(new ImageIcon(preImg));

		this.preview.setIcon(null);
		this.preview.setText("   Loading...   ");
		ObservableTPFilter filter = new ObservableTPFilter(
				this.genFunction.apply(preImg.getWidth(), preImg.getHeight()));
		this.executor.submit(() -> {
			BufferedImage preview = filter.apply(preImg, ITERATIONS_DEFAULT, SAMPLES_DEFAULT);
			SwingUtilities.invokeLater(() -> {
				this.preview.setText(null);
				this.preview.setIcon(new ImageIcon(preview));
			});
		});
	}

	/**
	 * Scales an image without distortion to match the specified borders.
	 * 
	 * @param src
	 *            source image
	 * @param maxWidth
	 *            width border
	 * @param maxHeight
	 *            height border
	 * @return the scaled image
	 */
	public static BufferedImage scaleToBorders(BufferedImage src, int maxWidth, int maxHeight) {
		double scale = Math.min((double) maxWidth / src.getWidth(), (double) maxHeight / src.getHeight());
		return scale(src, scale, scale);
	}

	/**
	 * Scales an image with the specified factors.
	 * 
	 * @param src
	 *            source image
	 * @param scaleWidth
	 *            width scale factor
	 * @param scaleHeight
	 *            height scale factor
	 * @return the scaled image
	 */
	public static BufferedImage scale(BufferedImage src, double scaleWidth, double scaleHeight) {
		BufferedImage dest = new BufferedImage((int) Math.ceil(src.getWidth() * scaleWidth),
				(int) Math.ceil(src.getHeight() * scaleHeight), src.getType());
		Graphics2D g = dest.createGraphics();
		g.drawRenderedImage(src, AffineTransform.getScaleInstance(scaleWidth, scaleHeight));
		return dest;
	}

	private void setUpComponents(JButton load, JButton run) {
		this.frame.setMinimumSize(new Dimension(400, 400));
		this.frame.setResizable(false);
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Illustrate.this.executor.shutdownNow();
			}
		});
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		load.setPreferredSize(new Dimension(100, 40));
		load.setFocusable(false);
		load.setToolTipText("Loads a picture and shows a preview.");
		load.addActionListener(e -> {
			int val = this.chooser.showOpenDialog(this.frame);
			if (val == JFileChooser.APPROVE_OPTION) {
				File file = this.chooser.getSelectedFile();
				try {
					BufferedImage img = ImageIO.read(file);
					// tests the size
					if (img.getWidth() > MAX_WIDTH || img.getHeight() > MAX_HEIGHT) {
						int option = JOptionPane.showConfirmDialog(this.frame,
								"<html>The selected image is too big for efficient calculation.<p>"
										+ "The image will be scaled down.</html>",
								"Scaling required", JOptionPane.OK_CANCEL_OPTION);
						if (option == JOptionPane.OK_OPTION) {
							img = scaleToBorders(img, MAX_WIDTH, MAX_HEIGHT);
						} else {
							return;
						}
					}
					this.setCurrentImage(img);
					this.fileName = file.getName();
				} catch (IOException exc) {
					JOptionPane.showMessageDialog(this.frame, "Error loading file.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		run.setPreferredSize(new Dimension(100, 40));
		run.setFocusable(false);
		run.setToolTipText("Starts the calculation.");
		run.addActionListener(e -> {
			FrameView view = new FrameView(this.fileName, this.currentImage,
					this.genFunction.apply(this.currentImage.getWidth(), this.currentImage.getHeight()),
					this.iterations.getValue(), this.samples.getValue(), this.chooser);
			JFrame vFrame = view.getFrame();
			vFrame.setLocationRelativeTo(this.frame);
			Point p = vFrame.getLocation();
			vFrame.setLocation(p.x + 100, p.y + 200);
			vFrame.setVisible(true);
			view.startCalculation();
		});

		try {
			this.setCurrentImage(ImageIO.read(this.getClass().getResource(DEFAULT_IMG)));
		} catch (IOException e) {
			this.currentImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
			this.original.setText("<html>   Failure loading<p>   default image.</html>");
		}
	}

	private void setUpLayout(JButton load, JButton run) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.insets = new Insets(5, 10, 5, 10);
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 2;
		panel.add(this.original, c);
		c.gridx = 1;
		panel.add(this.preview, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		panel.add(this.iterations.getComponent(), c);
		c.gridy = 2;
		panel.add(this.samples.getComponent(), c);
		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		panel.add(load, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		panel.add(run, c);
		this.frame.add(panel);
	}
}
