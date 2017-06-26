package gui;

import javax.swing.SwingUtilities;

import org.iMage.geometrify.Bounds;
import org.iMage.geometrify.PrimitiveGenerator;
import org.iMage.geometrify.TriangleGenerator;

/**
 * Main class for initialization of iLlustrate.
 * 
 * @author Nikolai
 */
public final class GUIApp {

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
		SwingUtilities.invokeLater(() -> {
			Illustrate il = new Illustrate((width, height) -> {
				PrimitiveGenerator gen = new TriangleGenerator(width, height).bindToArea(50, 50, 150, 150,
						Bounds.fixedBounds(150));
				// gen.setBounds(PolygonGenerator.RANDOM_BOUNDS);
				return gen;
			});
			il.getFrame().setLocationRelativeTo(null);
			il.getFrame().setVisible(true);
		});
	}

}
