package gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.iMage.geometrify.primitives.ColoredPrimitive;
import org.iMage.geometrify.primitives.Primitive;

import filter.FilterObserver;
import filter.ObservableTPFilter;

/**
 * An instance of this class holds all information required to display different
 * frames of the image calculation process.
 * 
 * @author Nikolai
 *
 */
public class FrameProvider extends StateChanger implements FilterObserver {
	private final ObservableTPFilter filter;
	private final List<ColoredPrimitive> primitives;
	private BufferedImage currentImage;
	private int index;
	boolean liveUpdating;

	/**
	 * Creates a FrameProvider for the specified filter and image.
	 * 
	 * @param filter
	 *            observable filter that is applied to the image
	 * @param init
	 *            initial image (usually empty)
	 */
	public FrameProvider(ObservableTPFilter filter, BufferedImage init) {
		this.filter = Objects.requireNonNull(filter);
		this.currentImage = init;
		this.primitives = new ArrayList<>();
		this.index = 0;
		this.filter.addObserver(this);
		this.liveUpdating = true;
	}

	@Override
	public void update(BufferedImage current, ColoredPrimitive added) {
		this.primitives.add(added);
		if (this.liveUpdating) {
			this.updateFrame(current, this.primitives.size());
		}
	}

	/**
	 * Gets whether the image is updated live with the calculation.
	 * 
	 * @return live updating
	 */
	public boolean isLiveUpdating() {
		return this.liveUpdating;
	}

	/**
	 * Sets whether the image should be updated live with the calculation.
	 * 
	 * @param flag
	 *            specifies live updating
	 */
	public void setLiveUpdating(boolean flag) {
		this.liveUpdating = flag;
	}

	/**
	 * Returns the image pf the currently set frame.
	 * 
	 * @return image of current frame
	 */
	public BufferedImage getCurrentFrame() {
		return this.currentImage;
	}

	/**
	 * Returns the index of the currently set frame.
	 * 
	 * @return index of current frame
	 */
	public int getCurrentIndex() {
		return this.index;
	}

	/**
	 * Return the number of applied primitives (= number of frames - 1).
	 * 
	 * @return number of primitives
	 */
	public int getSize() {
		return this.primitives.size();
	}

	/**
	 * Gets the primitive at the specified index.
	 * 
	 * @param index
	 *            index of the primitive
	 * @return the primitive
	 */
	public Primitive getPrimitive(int index) {
		if (index < 0 || index >= this.primitives.size()) {
			throw new IndexOutOfBoundsException("No primitive at this index available.");
		}
		return this.primitives.get(index);
	}

	/**
	 * Resets to the given image and removes all primitives.
	 * 
	 * @param init
	 *            resetting image
	 */
	public void reset(BufferedImage init) {
		this.currentImage = init;
		this.primitives.clear();
		this.index = 0;
	}

	/**
	 * Calculates the frame for the specified index. The image can be retrieved
	 * by getCurrentIndex(). Needs calculation time dependent on the set index.
	 * 
	 * @param newIndex
	 *            index of the frame to be calculated
	 */
	public void setFrame(int newIndex) {
		if (newIndex < 0 || newIndex > this.primitives.size()) {
			throw new IndexOutOfBoundsException("No frame at this index available.");
		}
		BufferedImage result = this.currentImage;
		int counter = this.index;
		if (newIndex < this.index) {
			result = new BufferedImage(this.currentImage.getWidth(), this.currentImage.getHeight(),
					this.currentImage.getType());
			counter = 0;
		}

		while (counter < newIndex) {
			if (Thread.interrupted()) {
				return;
			}
			ColoredPrimitive primitive = this.primitives.get(counter);
			this.filter.addToImage(result, primitive);
			counter++;
		}
		this.updateFrame(result, newIndex);
		assert counter == newIndex;
	}

	private void updateFrame(BufferedImage current, int newIndex) {
		this.currentImage = current;
		this.index = newIndex;
		SwingUtilities.invokeLater(() -> this.notifyListeners(new ChangeEvent(this)));
	}
}
