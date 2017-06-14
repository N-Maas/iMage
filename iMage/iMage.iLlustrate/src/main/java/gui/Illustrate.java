package gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.iMage.geometrify.RandomPointGenerator;

import filter.ObservableTPFilter;

public class Illustrate {
	private static final File DEFAULT_IMG_PATH = new File("src/main/resources/Default.png");
	private static final int ITERATIONS_MAX = 2000;
	private static final int ITERATIONS_DEFAULT = 100;
	private static final int SAMPLES_MAX = 200;
	private static final int SAMPLES_DEFAULT = 30;
	private static final int PREVIEW_WIDTH = 150;
	private static final int PREVIEW_HEIGHT = 150;

	private BufferedImage currentImage;
	private final JFrame frame;
	private final JLabel original;
	private final JLabel preview;
	private final LabeledSlider iterations;
	private final LabeledSlider samples;
	private final JFileChooser chooser;

	public Illustrate() {
		this.frame = new JFrame("iLlustrate");
		this.frame.setMinimumSize(new Dimension(400, 400));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// TODO
		this.frame.setResizable(false);

		this.original = new JLabel();
		this.original.setPreferredSize(new Dimension(150, 150));
		this.preview = new JLabel();
		this.preview.setPreferredSize(new Dimension(150, 150));
		this.iterations = new LabeledSlider("Iterations", ITERATIONS_MAX, ITERATIONS_DEFAULT);
		this.samples = new LabeledSlider("Samples   ", SAMPLES_MAX, SAMPLES_DEFAULT);
		this.chooser = new JFileChooser();
		this.chooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

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
		JButton load = new JButton("Load");
		load.setPreferredSize(new Dimension(100, 40));
		load.setFocusable(false);
		load.addActionListener(e -> {
			int val = this.chooser.showOpenDialog(this.frame);
			if (val == JFileChooser.APPROVE_OPTION) {
				File f = this.chooser.getSelectedFile();
				try {
					this.setCurrentImage(ImageIO.read(f));
				} catch (IOException exc) {
					// TODO Fehlermeldung
					exc.printStackTrace();
				}
			}
		});
		JButton run = new JButton("Run");
		run.setPreferredSize(new Dimension(100, 40));
		run.setFocusable(false);

		// TODO ACTION LISTENER

		c.gridy = 3;
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridwidth = 1;
		panel.add(load, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.WEST;
		panel.add(run, c);

		try {
			this.setCurrentImage(ImageIO.read(DEFAULT_IMG_PATH));
		} catch (IOException e) {
			this.currentImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
			this.original.setText("<html>Failure loading<p>default image.</html>");
		}

		this.frame.add(panel);
	}

	public JFrame getFrame() {
		return this.frame;
	}

	public final void setCurrentImage(BufferedImage img) {
		this.currentImage = img;
		BufferedImage preImg = scaleToBorders(img, PREVIEW_WIDTH, PREVIEW_HEIGHT);
		this.original.setIcon(new ImageIcon(preImg));

		// TODO Call Filter/Multithreading
		this.preview.setIcon(
				new ImageIcon(new ObservableTPFilter(new RandomPointGenerator(preImg.getWidth(), preImg.getHeight()))
						.apply(preImg, ITERATIONS_DEFAULT, SAMPLES_DEFAULT)));
	}

	public static BufferedImage scaleToBorders(BufferedImage src, int maxWidth, int maxHeight) {
		double scale = Math.min((double) maxWidth / src.getWidth(), (double) maxHeight / src.getHeight());
		return scale(src, scale, scale);
	}

	public static BufferedImage scale(BufferedImage src, double scaleWidth, double scaleHeight) {
		BufferedImage dest = new BufferedImage((int) Math.ceil(src.getWidth() * scaleWidth),
				(int) Math.ceil(src.getHeight() * scaleHeight), src.getType());
		Graphics2D g = dest.createGraphics();
		g.drawRenderedImage(src, AffineTransform.getScaleInstance(scaleWidth, scaleHeight));
		return dest;
	}
}
