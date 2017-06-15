package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Represents a slider combined with a JLabel that displays the current value.
 * 
 * @author Nikolai
 */
public class LabeledSlider {
	private final List<ChangeListener> cls = new ArrayList<>();
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
		this(labelText, 0, max, value, 0, true);
	}

	/**
	 * Creates a LabeledSlider with the specified label text, max value, initial
	 * value and preferred width for the slider.
	 * 
	 * @param labelText
	 *            text identifier for the value of the slider
	 * @param max
	 *            max value
	 * @param value
	 *            initial value
	 * @param prefWidth
	 *            preferred width of the slider
	 */
	public LabeledSlider(String labelText, int max, int value, int prefWidth) {
		this(labelText, 0, max, value, prefWidth, true);
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
	 */
	public LabeledSlider(String labelText, int min, int max, int value, int prefWidth, boolean labelLeft) {
		this.labelText = labelText;
		this.panel = new JPanel();
		BoxLayout layout = new BoxLayout(this.panel, SwingConstants.HORIZONTAL);
		this.panel.setLayout(layout);
		this.label = new JLabel(this.labelText + " (" + value + ")");
		this.label.setAlignmentY(1);

		this.slider = new JSlider(min, max, value);
		this.setUpSlider(min, max, value, prefWidth, labelLeft);
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
	 * Adds a ChangeListener that is only invoked after adjusting of the value.
	 * 
	 * @param cl
	 *            listener
	 */
	public void addChangeListener(ChangeListener cl) {
		this.cls.add(cl);
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
		this.notify(new ChangeEvent(this));
	}

	private void adjustValue() {
		this.label.setText(this.labelText + " (" + this.getValue() + ")");
	}

	private void notify(ChangeEvent e) {
		for (ChangeListener cl : this.cls) {
			cl.stateChanged(e);
		}
	}

	private void setUpSlider(int min, int max, int value, int prefWidth, boolean labelLeft) {
		this.slider.setAlignmentY(0.5f);
		this.slider.setPaintTicks(true);
		this.slider.setMajorTickSpacing(max - min);
		this.slider.setPaintLabels(true);
		this.slider.setLabelTable(this.slider.createStandardLabels(max - min));
		this.slider.addChangeListener(e -> this.adjustValue());
		this.slider.addChangeListener(e -> {
			if (!this.slider.getValueIsAdjusting()) {
				this.notify(new ChangeEvent(this));
			}
		});
		if (prefWidth > 0) {
			Dimension prefSize = this.slider.getPreferredSize();
			this.slider.setPreferredSize(new Dimension(prefWidth, (int) prefSize.getHeight()));
			this.slider.setMaximumSize(new Dimension(prefWidth, (int) prefSize.getHeight()));
		} else {
			Dimension prefSize = this.label.getPreferredSize();
			this.label.setPreferredSize(new Dimension((int) prefSize.getWidth() + 10, (int) prefSize.getHeight()));
		}

		this.panel.add(labelLeft ? this.label : this.slider);
		this.panel.add(prefWidth <= 0 ? Box.createHorizontalStrut(10) : Box.createHorizontalGlue());
		this.panel.add(labelLeft ? this.slider : this.label);
	}
}
