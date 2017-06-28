package gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.iMage.geometrify.generators.BindablePrimitiveGenerator;
import org.iMage.geometrify.generators.Bounds;

public class PrimitiveTypeChooser {
	private final JDialog dialog;
	private final TypeSelecter<BindablePrimitiveGenerator> primitiveSelecter = new TypeSelecter<>("Primitive: ", 200);
	private final TypeSelecter<Bounds> boundsSelecter = new TypeSelecter<>("Bounds: ", 200);
	private boolean confirmed = false;

	public PrimitiveTypeChooser(Frame owner, String title) {
		this(owner, title, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
	}

	public PrimitiveTypeChooser(Frame owner, String title, List<SelectableType<BindablePrimitiveGenerator>> primitives,
			List<SelectableType<Bounds>> bounds) {
		this.dialog = new JDialog(owner, title, true);
		this.dialog.setResizable(false);
		this.dialog.setLocationRelativeTo(owner);
		primitives.forEach(this.primitiveSelecter::add);
		bounds.forEach(this.boundsSelecter::add);

		JPanel panel = new JPanel();
		JButton confirmButton = new JButton("OK");
		confirmButton.addActionListener(e -> {
			this.confirmed = true;
			this.dialog.setVisible(false);
		});
		confirmButton.setPreferredSize(new Dimension(100, 40));
		confirmButton.setMaximumSize(new Dimension(100, 40));
		confirmButton.setAlignmentX(0f);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.primitiveSelecter.getComponent());
		panel.add(this.boundsSelecter.getComponent());
		panel.add(confirmButton);
		panel.add(Box.createVerticalStrut(10));
		this.dialog.add(panel);
	}

	public TypeSelecter<BindablePrimitiveGenerator> getPrimitiveSelecter() {
		return this.primitiveSelecter;
	}

	public TypeSelecter<Bounds> getBoundsSelecter() {
		return this.boundsSelecter;
	}

	public BindablePrimitiveGenerator getPrimitiveGenerator() {
		BindablePrimitiveGenerator gen = this.primitiveSelecter.getSelected().getValue();
		return gen.bind(this.boundsSelecter.getSelected().getValue());
	}

	public BindablePrimitiveGenerator getPrimitiveGenerator(int x, int y, int widht, int height) {
		BindablePrimitiveGenerator gen = this.primitiveSelecter.getSelected().getValue();
		return gen.rebind(x, y, widht, height, this.boundsSelecter.getSelected().getValue());
	}

	public boolean show() {
		this.dialog.pack();
		SelectableType<BindablePrimitiveGenerator> oldPrimitive = this.primitiveSelecter.getSelected();
		SelectableType<Bounds> oldBounds = this.boundsSelecter.getSelected();
		this.dialog.setVisible(true);
		if (!this.confirmed) {
			this.primitiveSelecter.setSelected(oldPrimitive);
			this.boundsSelecter.setSelected(oldBounds);
		}
		boolean result = this.confirmed;
		this.confirmed = false;
		return result;
	}

	public JDialog getDialog() {
		return this.dialog;
	}
}
