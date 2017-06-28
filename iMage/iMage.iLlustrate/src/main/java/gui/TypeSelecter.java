package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TypeSelecter<T> {
	private final Set<SelectableType<T>> types = new HashSet<>();
	private final JComboBox<SelectableType<T>> box = new JComboBox<>();
	private final CardLayout layout = new CardLayout();
	private final JPanel typePanel = new JPanel(this.layout);
	private final JPanel panel = new JPanel(new BorderLayout());

	public TypeSelecter(String text, int boxSize) {
		this.box.addActionListener(e -> {
			this.setSelected(this.getSelected());
		});
		this.box.setMaximumSize(new Dimension(boxSize, this.box.getPreferredSize().height));
		this.box.setPreferredSize(new Dimension(boxSize, this.box.getPreferredSize().height));
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
		topPanel.add(new JLabel(text));
		topPanel.add(Box.createGlue());
		topPanel.add(this.box);

		this.panel.setLayout(new BorderLayout());
		this.panel.add(topPanel, BorderLayout.NORTH);
		this.panel.add(this.typePanel, BorderLayout.CENTER);
	}

	public void add(SelectableType<T> type) {
		this.add(type, 0);
	}

	public void add(SelectableType<T> type, int position) {
		if (this.types.contains(Objects.requireNonNull(type))) {
			throw new IllegalStateException("Type already contained.");
		}
		if (position < 0 || position > this.types.size()) {
			throw new IllegalArgumentException("Illegal position.");
		}
		this.types.add(type);
		this.box.insertItemAt(type, position);
		this.typePanel.add(type.getComponent());
		this.layout.addLayoutComponent(type.getComponent(), type.toString());
		this.setSelected(type);
	}

	public boolean remove(SelectableType<T> type) {
		if (!this.types.contains(type)) {
			return false;
		}
		this.layout.removeLayoutComponent(type.getComponent());
		this.types.remove(type);
		this.box.removeItem(type);
		return true;
	}

	public SelectableType<T> getSelected() {
		return (SelectableType<T>) this.box.getSelectedItem();
	}

	public boolean setSelected(SelectableType<T> type) {
		if (!this.types.contains(type)) {
			return false;
		}
		this.box.setSelectedItem(type);
		this.layout.show(this.typePanel, type.toString());
		return true;

	}

	public JComponent getComponent() {
		return this.panel;
	}
}
