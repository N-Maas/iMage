package gui;

import java.util.function.Supplier;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class SelectableType<T> {
	private final String name;

	protected SelectableType(String name) {
		this.name = name;
	}

	public abstract JComponent getComponent();

	public abstract T getValue();

	@Override
	public final String toString() {
		return this.name;
	}

	// not implemented for purpose of assuring equality of returned components
	//
	// @Override
	// public final int hashCode() {
	// return 31 * this.name.hashCode();
	// }
	//
	// @Override
	// public final boolean equals(Object obj) {
	// if (obj == null || this.getClass() != obj.getClass()) {
	// return false;
	// }
	// return ((SelectableType) obj).name.equals(this.name);
	// }

	static <T> SelectableType<T> create(String name, T value) {
		return create(name, () -> value);
	}

	static <T> SelectableType<T> create(String name, Supplier<T> supplier) {
		return create(name, supplier, new JPanel());
	}

	static <T> SelectableType<T> create(String name, Supplier<T> supplier, JComponent component) {
		return new SelectableType<T>(name) {

			@Override
			public JComponent getComponent() {
				return component;
			}

			@Override
			public T getValue() {
				return supplier.get();
			}
		};
	}
}
