package gui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

/**
 * Represents a slider combined with a JLabel that displays the current value.
 * 
 * @author Nikolai
 */
public class LabeledSlider extends StateChanger {
	private final JPanel panel;
	private final JSlider slider;
	private final JLabel label;
	private final String labelText;

	/**
	 * Creates a LabeledSlider with the specified label text, max value und
	 * initial value.
	 * 
	 * @param labelText
	 *            text identifier for the value of the slider
	 * @param max
	 *            max value
	 * @param value
	 *            initial value
	 */
	public LabeledSlider(String labelText, int max, int value) {
		this(labelText, max, value, 0, 0, 10);
	}

	/**
	 * Creates a LabeledSlider with the specified label text, puffer size, max
	 * value, initial value and preferred width for the slider.
	 * 
	 * @param labelText
	 *            text identifier for the value of the slider
	 * @param max
	 *            max value
	 * @param value
	 *            initial value
	 * @param prefWidth
	 *            preferred width of the slider
	 * @param puffer
	 *            value that is added to the width of the label, so it can hold
	 *            values with more digits
	 */
	public LabeledSlider(String labelText, int max, int value, int prefWidthL, int prefWidthS, int puffer) {
		this(labelText, 0, max, value, prefWidthL, prefWidthS, puffer, true);
	}

	/**
	 * Creates a LabeledSlider with the specified label text, min value, max
	 * value, initial value and preferred width for the slider. Can specify
	 * where to align the label.
	 * 
	 * @param labelText
	 *            text identifier for the value of the slider
	 * @param min
	 *            min value
	 * @param max
	 *            max value
	 * @param value
	 *            initial value
	 * @param prefWidth
	 *            preferred width of the slider
	 * @param labelLeft
	 *            if true, the label is aligned left of the slider, otherwise
	 *            right
	 * @param puffer
	 *            value that is added to the width of the label, so it can hold
	 *            values with more digits
	 */
	public LabeledSlider(String labelText, int min, int max, int value, int prefWidthL, int prefWidth, int puffer,
			boolean labelLeft) {
		this.labelText = labelText;
		this.panel = new JPanel();
		BoxLayout layout = new BoxLayout(this.panel, BoxLayout.X_AXIS);
		this.panel.setLayout(layout);
		this.label = new JLabel(this.labelText + " (" + value + ")");
		this.label.setAlignmentY(1);

		this.slider = new JSlider(min, max, value);
		this.setUpSlider(min, max, value, prefWidthL, prefWidth, puffer, labelLeft);
	}

	/**
	 * Returns the JComponent containing the gui elements.
	 * 
	 * @return gui component
	 */
	public JComponent getComponent() {
		return this.panel;
	}

	/**
	 * Returns the slider.
	 * 
	 * @return slider
	 */
	public JSlider getSlider() {
		return this.slider;
	}

	/**
	 * Returns the current value of the slider.
	 * 
	 * @return the current value
	 */
	public int getValue() {
		return this.slider.getValue();
	}

	/**
	 * Sets the current value.
	 * 
	 * @param value
	 *            new value
	 * @throws IllegalArgumentException
	 *             if the value is out of the slider bounds
	 */
	public void setValue(int value) {
		if (value < this.slider.getMinimum() || value > this.slider.getMaximum()) {
			throw new IllegalArgumentException("Value out of bounds: " + value);
		}
		this.slider.setValue(value);
		this.adjustValue();
		this.notifyListeners(new ChangeEvent(this));
		this.slider.repaint();
	}

	private void adjustValue() {
		this.label.setText(this.labelText + " (" + this.getValue() + ")");
	}

	private void setUpSlider(int min, int max, int value, int prefWidthL, int prefWidthS, int puffer,
			boolean labelLeft) {
		Dimension labelPrefSize = this.label.getPreferredSize();
		this.label.setPreferredSize(
				new Dimension((int) labelPrefSize.getWidth() + puffer, (int) labelPrefSize.getHeight()));

		this.slider.setAlignmentY(0.5f);
		this.slider.setPaintTicks(true);
		this.slider.setMajorTickSpacing(max - min);
		this.slider.setPaintLabels(true);
		this.slider.setLabelTable(this.slider.createStandardLabels(max - min));
		this.slider.addChangeListener(e -> this.adjustValue());
		this.slider.addChangeListener(e -> {
			if (!this.slider.getValueIsAdjusting()) {
				this.notifyListeners(new ChangeEvent(this));
			}
		});

		if (prefWidthS > 0) {
			Dimension prefSizeS = this.slider.getPreferredSize();
			this.slider.setPreferredSize(new Dimension(prefWidthS, (int) prefSizeS.getHeight()));
			this.slider.setMaximumSize(new Dimension(prefWidthS, (int) prefSizeS.getHeight()));
		}
		if (prefWidthL > 0) {
			Dimension prefSizeL = this.label.getPreferredSize();
			this.label.setPreferredSize(new Dimension(prefWidthL, (int) prefSizeL.getHeight()));
			this.label.setMaximumSize(new Dimension(prefWidthL, (int) prefSizeL.getHeight()));
		}
		this.panel.add(labelLeft ? this.label : this.slider);
		if (prefWidthS > 0 && prefWidthL > 0) {
			this.panel.add(Box.createHorizontalGlue());
		}
		this.panel.add(Box.createHorizontalStrut(10));
		this.panel.add(labelLeft ? this.slider : this.label);
	}
}
