package filter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.iMage.geometrify.IPointGenerator;
import org.iMage.geometrify.IPrimitive;
import org.iMage.geometrify.TrianglePictureFilter;

/**
 * Subclass of TrianglePictureFilter that adds observable functionality.
 * 
 * @author Nikolai
 */
public class ObservableTPFilter extends TrianglePictureFilter implements FilterObservable {
	private final List<FilterObserver> obs = new ArrayList<>();

	/**
	 * Creates an ObservableTPFilter.
	 * 
	 * @param pointGenerator
	 *            the RandomPointGenerator
	 */
	public ObservableTPFilter(IPointGenerator pointGenerator) {
		super(pointGenerator);
	}

	@Override
	public void addObserver(FilterObserver fo) {
		this.obs.add(Objects.requireNonNull(fo));
	}

	@Override
	public boolean deleteObserver(FilterObserver fo) {
		return this.obs.remove(fo);
	}

	@Override
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples) {
		int width = image.getWidth();
		int height = image.getHeight();

		// construct "empty" image
		BufferedImage result = new BufferedImage(width, height, image.getType());

		for (int i = 0; i < numberOfIterations; i++) {
			int bestDifference = Integer.MAX_VALUE;
			IPrimitive bestPrimitive = null;

			for (int s = 0; s < numberOfSamples; s++) {
				if (Thread.interrupted()) {
					return result;
				}
				IPrimitive sample = this.generatePrimitive();
				sample.setColor(this.calculateColor(image, sample));
				int difference = this.calculateDifference(image, result, sample);

				if (difference <= bestDifference) {
					bestDifference = difference;
					bestPrimitive = sample;
				}
			}

			this.addToImage(result, bestPrimitive);
			this.notifyObservers(result, bestPrimitive);
		}

		return result;
	}

	@Override
	public void addToImage(BufferedImage current, IPrimitive primitive) {
		super.addToImage(current, primitive);
	}

	/**
	 * Updates all registered observers.
	 * 
	 * @param img
	 *            current image
	 * @param ip
	 *            last added primitive
	 */
	protected void notifyObservers(BufferedImage img, IPrimitive ip) {
		for (FilterObserver fObs : this.obs) {
			fObs.update(img, ip);
		}
	}
}
