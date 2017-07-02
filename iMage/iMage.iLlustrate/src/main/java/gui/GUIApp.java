package gui;

import java.awt.GridLayout;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.iMage.geometrify.generators.BindablePrimitiveGenerator;
import org.iMage.geometrify.generators.Bounds;
import org.iMage.geometrify.generators.CircleGenerator;
import org.iMage.geometrify.generators.EllipseGenerator;
import org.iMage.geometrify.generators.PolygonGenerator;
import org.iMage.geometrify.generators.RectangleGenerator;
import org.iMage.geometrify.generators.SquareGenerator;
import org.iMage.geometrify.generators.StarGenerator;

/**
 * Main class for initialization of iLlustrate.
 * 
 * @author Nikolai
 */
public final class GUIApp {
	private static final PropertyChangeListener PERCENTAGE_LISTENER = evt -> {
		JFormattedTextField source = (JFormattedTextField) evt.getSource();
		double value = ((Number) source.getValue()).doubleValue();
		if (value < 0) {
			source.setValue(0d);
		} else if (value > 1) {
			source.setValue(1d);
		}
	};

	/**
	 * Utility class constructor.
	 */
	private GUIApp() {
	}

	/**
	 * Main method. Creates an instance of Illustrate and shows it.
	 * 
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args) {
		List<SelectableType<BindablePrimitiveGenerator>> primitives = new ArrayList<>();
		JPanel starPanel = new JPanel();
		starPanel.setLayout(new GridLayout(3, 2));
		JFormattedTextField starMin = createNumberField(5, 2);
		starPanel.add(createValuePane("Minimal corners:", starMin));
		JFormattedTextField starMax = createNumberField(5, 2);
		starPanel.add(createValuePane("Maximal corners:", starMax));
		JFormattedTextField innerRadian = createPercentageField(0.5);
		starPanel.add(createValuePane("Inner radian:", innerRadian));
		JFormattedTextField arcVariance = createPercentageField();
		starPanel.add(createValuePane("Arc variance:", arcVariance));
		JFormattedTextField innerVariance = createPercentageField();
		starPanel.add(createValuePane("Inner variance:", innerVariance));
		JFormattedTextField outerVariance = createPercentageField();
		starPanel.add(createValuePane("Outer variance:", outerVariance));
		primitives.add(SelectableType.create("Star", () -> {
			int min = ((Number) starMin.getValue()).intValue();
			int max = ((Number) starMax.getValue()).intValue();
			if (max < min) {
				starMax.setValue((long) min);
				max = min;
			}
			StarGenerator result = new StarGenerator(1, 1, min, max);
			result.setInnerRadian(((Number) innerRadian.getValue()).doubleValue());
			result.setArcVariance(((Number) arcVariance.getValue()).doubleValue());
			result.setInnerVariance(((Number) innerVariance.getValue()).doubleValue());
			result.setOuterVariance(((Number) outerVariance.getValue()).doubleValue());
			return result;
		}, starPanel));

		JPanel polyPanel = new JPanel();
		polyPanel.setLayout(new GridLayout(3, 2));
		JFormattedTextField polyMin = createNumberField(4, 3);
		polyPanel.add(createValuePane("Minimal corners:", polyMin));
		JFormattedTextField polyMax = createNumberField(8, 3);
		polyPanel.add(createValuePane("Maximal corners:", polyMax));
		polyPanel.add(new JPanel());
		polyPanel.add(new JPanel());
		polyPanel.add(new JPanel());
		polyPanel.add(new JPanel());
		primitives.add(SelectableType.create("Polygon", () -> {
			int min = ((Number) polyMin.getValue()).intValue();
			int max = ((Number) polyMax.getValue()).intValue();
			if (max < min) {
				polyMax.setValue((long) min);
				max = min;
			}
			return new PolygonGenerator(1, 1, min, max);
		}, polyPanel));

		primitives.add(SelectableType.create("Circle", new CircleGenerator(1, 1)));
		primitives.add(SelectableType.create("Ellipse", new EllipseGenerator(1, 1)));
		primitives.add(SelectableType.create("Square", new SquareGenerator(1, 1)));
		primitives.add(SelectableType.create("Rectangle", new RectangleGenerator(1, 1)));
		primitives.add(SelectableType.create("Triangle", new PolygonGenerator(1, 1)));

		List<SelectableType<Bounds>> bounds = new ArrayList<>();
		JPanel relativePanel = new JPanel();
		relativePanel.setLayout(new GridLayout(1, 2));
		JFormattedTextField relativeX = createPercentageField(0.25);
		relativePanel.add(createValuePane("Width border:", relativeX));
		JFormattedTextField relativeY = createPercentageField(0.25);
		relativePanel.add(createValuePane("Height border:", relativeY));
		bounds.add(SelectableType.create("Relative bounds",
				() -> Bounds.relativeBounds(((Number) relativeX.getValue()).doubleValue(),
						((Number) relativeY.getValue()).doubleValue()),
				relativePanel));

		JPanel fixPanel = new JPanel();
		fixPanel.setLayout(new GridLayout(1, 2));
		JFormattedTextField fixX = createNumberField(100, 2);
		fixPanel.add(createValuePane("Width border:", fixX));
		JFormattedTextField fixY = createNumberField(100, 2);
		fixPanel.add(createValuePane("Height border:", fixY));
		bounds.add(SelectableType.create("Fixed bounds",
				() -> Bounds.fixedBounds(((Number) fixX.getValue()).intValue(), ((Number) fixY.getValue()).intValue()),
				fixPanel));

		bounds.add(SelectableType.create("Random bounds (linear)", Bounds.RANDOM_LINEAR_BOUNDS));
		bounds.add(SelectableType.create("Random bounds (scaling)", Bounds.RANDOM_SCALING_BOUNDS));
		bounds.add(SelectableType.create("No bounds", Bounds.NO_BOUNDS));
		SwingUtilities.invokeLater(() -> {
			Illustrate il = new Illustrate(primitives, bounds);
			il.getFrame().setLocationRelativeTo(null);
			il.getFrame().setVisible(true);
		});
	}

	private static PropertyChangeListener ensureMinListener(long min) {
		return evt -> {
			JFormattedTextField source = (JFormattedTextField) evt.getSource();
			if (((Number) source.getValue()).longValue() < min) {
				source.setValue(min);
			}
		};
	}

	private static JFormattedTextField createPercentageField() {
		return createPercentageField(0);
	}

	private static JFormattedTextField createPercentageField(double initialValue) {
		JFormattedTextField result = new JFormattedTextField(NumberFormat.getPercentInstance());
		result.setValue(initialValue);
		result.addPropertyChangeListener(PERCENTAGE_LISTENER);
		return result;
	}

	private static JFormattedTextField createNumberField(int initialValue, int min) {
		JFormattedTextField result = new JFormattedTextField(NumberFormat.getIntegerInstance());
		result.addPropertyChangeListener(ensureMinListener(min));
		result.setValue(initialValue);
		return result;
	}

	private static JComponent createValuePane(String text, JTextField tf) {
		JPanel result = new JPanel();
		result.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		result.setLayout(new BoxLayout(result, BoxLayout.Y_AXIS));
		result.add(new JLabel(text));
		result.add(Box.createVerticalStrut(5));
		result.add(tf);
		return result;
	}
}
