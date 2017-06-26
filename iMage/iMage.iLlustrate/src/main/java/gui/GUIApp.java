package gui;

import javax.swing.SwingUtilities;

import org.iMage.geometrify.PolygonGenerator;

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
				PolygonGenerator gen = new PolygonGenerator(width, height);
				gen.setBounds(PolygonGenerator.RANDOM_BOUNDS);
				return gen;
			});
			il.getFrame().setLocationRelativeTo(null);
			il.getFrame().setVisible(true);
		});
	}

}
