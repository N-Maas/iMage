package gui;

import javax.swing.SwingUtilities;

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
			Illustrate il = new Illustrate((width, height) -> new TriangleGenerator(width, height));
			il.getFrame().setLocationRelativeTo(null);
			il.getFrame().setVisible(true);
		});
	}

}
