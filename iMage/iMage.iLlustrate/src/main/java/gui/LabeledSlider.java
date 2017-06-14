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

public class LabeledSlider {
	private final List<ChangeListener> cls = new ArrayList<>();
	private final JPanel panel;
	private final JSlider slider;
	private final JLabel label;
	private final String labelText;

	public LabeledSlider(String labelText, int max, int value) {
		this(labelText, 0, max, value, 0, true);
	}

	public LabeledSlider(String labelText, int max, int value, int prefWidth) {
		this(labelText, 0, max, value, prefWidth, true);
	}

	public LabeledSlider(String labelText, int min, int max, int value, int prefWidth, boolean labelLeft) {
		this.labelText = labelText;
		this.panel = new JPanel();
		BoxLayout layout = new BoxLayout(this.panel, SwingConstants.HORIZONTAL);
		this.panel.setLayout(layout);
		this.label = new JLabel(this.labelText + " (" + value + ")");
		this.label.setAlignmentY(1);

		this.slider = new JSlider(min, max, value);
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

	public JComponent getComponent() {
		return this.panel;
	}

	public JSlider getSlider() {
		return this.slider;
	}

	public int getValue() {
		return this.slider.getValue();
	}

	public void addChangeListener(ChangeListener cl) {
		this.cls.add(cl);
	}

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
}
