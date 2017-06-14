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
import javax.swing.event.ChangeListener;

public class LabeledSlider {
	private final List<ChangeListener> cls = new ArrayList<>();
	private final JPanel panel;
	private final JSlider slider;
	private final JLabel label;

	public LabeledSlider(String labelText, int max, int value) {
		this(labelText, 0, max, value, true);
	}

	public LabeledSlider(String labelText, int min, int max, int value, boolean labelLeft) {
		this.panel = new JPanel();
		BoxLayout layout = new BoxLayout(this.panel, SwingConstants.HORIZONTAL);
		this.panel.setLayout(layout);
		this.label = new JLabel(labelText + " (" + value + ")");
		Dimension prefSize = this.label.getPreferredSize();
		this.label.setPreferredSize(new Dimension((int) prefSize.getWidth() + 10, (int) prefSize.getHeight()));
		this.label.setAlignmentY(1);

		this.slider = new JSlider(min, max, value);
		this.slider.setAlignmentY(0.5f);
		this.slider.setPaintTicks(true);
		this.slider.setMajorTickSpacing(max - min);
		this.slider.setPaintLabels(true);
		this.slider.setLabelTable(this.slider.createStandardLabels(max - min));
		this.slider.addChangeListener(e -> this.label.setText(labelText + " (" + this.getValue() + ")"));
		this.slider.addChangeListener(e -> {
			if (!this.slider.getValueIsAdjusting()) {
				for (ChangeListener cl : this.cls) {
					cl.stateChanged(e);
				}
			}
		});

		this.panel.add(labelLeft ? this.label : this.slider);
		this.panel.add(Box.createHorizontalStrut(10));
		this.panel.add(labelLeft ? this.slider : this.label);
	}

	public JComponent getComponent() {
		return this.panel;
	}

	public int getValue() {
		return this.slider.getValue();
	}

	public void addChangeListener(ChangeListener cl) {
		this.cls.add(cl);
	}

	// TODO setValue
}
