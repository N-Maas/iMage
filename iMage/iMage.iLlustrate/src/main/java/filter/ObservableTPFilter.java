package filter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.iMage.geometrify.GeneralPrimitivePictureFilter;
import org.iMage.geometrify.generators.PrimitiveGenerator;
import org.iMage.geometrify.primitives.ColoredPrimitive;

/**
 * Subclass of TrianglePictureFilter that adds observable functionality.
 * 
 * @author Nikolai
 */
public class ObservableTPFilter extends GeneralPrimitivePictureFilter implements FilterObservable {
	private final List<FilterObserver> obs = new ArrayList<>();
	private BufferedImage currentImage = null;

	/**
	 * Creates an ObservableTPFilter.
	 * 
	 * @param pointGenerator
	 *            the RandomPointGenerator
	 */
	public ObservableTPFilter(PrimitiveGenerator generator) {
		super(generator);
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
	public synchronized BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples) {
		this.currentImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
		return super.apply(image, numberOfIterations, numberOfSamples);
	}

	@Override
	protected void processIteration(int[][] newData, ColoredPrimitive primitive) {
		this.addToImage(this.currentImage, primitive);
		this.notifyObservers(this.currentImage, primitive);
	}

	/**
	 * Updates all registered observers.
	 * 
	 * @param img
	 *            current image
	 * @param ip
	 *            last added primitive
	 */
	protected void notifyObservers(BufferedImage img, ColoredPrimitive cp) {
		for (FilterObserver fObs : this.obs) {
			fObs.update(img, cp);
		}
	}
}
