package filter;

import java.awt.image.BufferedImage;

import org.iMage.geometrify.IPrimitive;

/**
 * An observer that observes Filters and receives primitives and new image
 * versions.
 * 
 * @author Nikolai
 */
public interface FilterObserver {

	/**
	 * Updates the observer. Should be called by observables.
	 * 
	 * @param current
	 *            current version of the calculated image
	 * @param added
	 *            last added primitive
	 */
	void update(BufferedImage current, IPrimitive added);
}
