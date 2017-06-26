package filter;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.iMage.geometrify.ColoredPrimitive;
import org.iMage.geometrify.GeneralPrimitivePictureFilter;
import org.iMage.geometrify.Primitive;
import org.iMage.geometrify.IPrimitiveGenerator;

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
	public ObservableTPFilter(IPrimitiveGenerator generator) {
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
	protected void processIteration(int[][] newData, Primitive primitive, Color c) {
		this.addToImage(this.currentImage, primitive, c);
		this.notifyObservers(this.currentImage, new ColoredPrimitive(primitive, c));
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
