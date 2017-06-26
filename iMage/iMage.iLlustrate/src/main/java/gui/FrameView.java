package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeListener;

import org.iMage.geometrify.PrimitiveGenerator;

import filter.ObservableTPFilter;

/**
 * Applies the filter to the image and holds a frame that can shows every step
 * of the calculation. Uses a FrameProvider for holding the calculation data.
 * 
 * @author Nikolai
 */
public class FrameView {
	private final ExecutorService executor;
	private final Runnable task;
	private final FrameProvider provider;
	private final JFrame frame;

	/**
	 * Creates a new FrameView with the specified parameters.
	 * 
	 * @param fileName
	 *            name of the file that provides the image
	 * @param src
	 *            source image for the calculation
	 * @param iterations
	 *            number of iterations
	 * @param samples
	 *            number of samples
	 * @param chooser
	 *            JFileChooser for saving the results
	 */
	public FrameView(String fileName, BufferedImage src, PrimitiveGenerator generator, int iterations, int samples,
			JFileChooser chooser) {
		int width = src.getWidth();
		int height = src.getHeight();
		this.executor = Executors.newSingleThreadExecutor();
		this.frame = new JFrame(fileName + " (" + iterations + " iterations, " + samples + " samples)");
		ObservableTPFilter filter = new ObservableTPFilter(generator);
		this.provider = new FrameProvider(filter, new BufferedImage(width, height, src.getType()));

		this.task = this.setUp(filter, src, iterations, samples, chooser);
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
	 * Returns the FrameProvider that holds the data for displaying the image.
	 * 
	 * @return the provider
	 */
	public FrameProvider getProvider() {
		return this.provider;
	}

	/**
	 * Starts the calculation. Returns immediately, passing the calculation to a
	 * parallel thread.
	 */
	public void startCalculation() {
		if (this.provider.getSize() > 0) {
			throw new IllegalStateException("Image already calculated.");
		}
		this.executor.submit(this.task);
	}

	private Runnable setUp(ObservableTPFilter filter, BufferedImage src, int iterations, int samples,
			JFileChooser chooser) {
		this.frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				FrameView.this.executor.shutdownNow();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				FrameView.this.executor.shutdownNow();
			}
		});
		this.frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JCheckBox check = new JCheckBox("Continuous Updates");
		LabeledSlider slider = new LabeledSlider("Snapshot", 0, iterations, 0, 0, 30, false);
		JButton save = new JButton("Save");
		JPanel imagePanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(FrameView.this.provider.getCurrentFrame(), 0, 0, null);
			}
		};
		imagePanel.setPreferredSize(new Dimension(src.getWidth(), src.getHeight()));

		check.setSelected(true);
		check.setFocusable(false);
		slider.getSlider().setEnabled(false);
		save.setPreferredSize(new Dimension(100, 40));
		save.setFocusable(false);
		save.setEnabled(false);
		save.setToolTipText("Saves the picture at the displayed state.");
		save.addActionListener(e -> {
			int val = chooser.showSaveDialog(this.frame);
			if (val == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				if (!file.getName().endsWith(".png")) {
					file = new File(file.getPath() + ".png");
				}
				try {
					ImageIO.write(this.provider.getCurrentFrame(), "png", file);
				} catch (IOException exc) {
					JOptionPane.showMessageDialog(this.frame, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		ChangeListener cl = e -> slider.setValue(this.provider.getCurrentIndex());
		this.provider.addChangeListener(cl);
		this.provider.addChangeListener(e -> this.frame.repaint());
		check.addChangeListener(e -> this.provider.setLiveUpdating(check.isSelected()));

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(check);
		panel.add(Box.createHorizontalGlue());
		panel.add(save);
		panel.add(Box.createHorizontalStrut(10));

		this.frame.add(imagePanel);
		this.frame.add(panel, BorderLayout.NORTH);
		this.frame.add(slider.getComponent(), BorderLayout.SOUTH);

		this.frame.pack();
		this.frame.setResizable(false);

		return () -> {
			filter.apply(src, iterations, samples);
			this.provider.setFrame(this.provider.getSize());
			SwingUtilities.invokeLater(() -> {
				slider.addChangeListener(e -> {
					this.executor.submit(() -> {
						this.provider.setFrame(slider.getValue());
					});
				});
				this.frame.repaint();
				this.provider.removeChangeListener(cl);
				check.setSelected(false);
				check.setEnabled(false);
				slider.getSlider().setEnabled(true);
				save.setEnabled(true);
			});
		};
	}
}