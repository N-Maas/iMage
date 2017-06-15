package gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeEvent;

import org.iMage.geometrify.IPrimitive;

import filter.FilterObserver;
import filter.ObservableTPFilter;

public class ImageProgression extends StateChanger implements FilterObserver {
	private final ObservableTPFilter filter;
	private final List<IPrimitive> primitives;
	private BufferedImage currentImage;
	private int index;
	boolean updateImage;

	public ImageProgression(ObservableTPFilter filter, BufferedImage init) {
		this.filter = filter;
		this.currentImage = init;
		this.primitives = new ArrayList<>();
		this.index = 0;
		this.filter.addObserver(this);
		this.updateImage = true;
	}

	@Override
	public void update(BufferedImage current, IPrimitive added) {
		this.primitives.add(added);
		if (this.updateImage) {
			this.updateFrame(current, this.primitives.size() - 1);
		}
	}

	public boolean isUpdatingImage() {
		return this.updateImage;
	}

	public void setImageUpdating(boolean flag) {
		this.updateImage = flag;
	}

	public BufferedImage getCurrentFrame() {
		return this.currentImage;
	}

	public int getCurrentIndex() {
		return this.index;
	}

	public int getSize() {
		return this.primitives.size();
	}

	public IPrimitive getPrimitive(int index) {
		if (index < 0 || index >= this.primitives.size()) {
			throw new IndexOutOfBoundsException("No primitive at this index available.");
		}
		return this.primitives.get(index);
	}

	public void setFrame(int newIndex) {
		if (newIndex < 0 || newIndex > this.primitives.size()) {
			throw new IndexOutOfBoundsException("No frame at this index available.");
		}
		BufferedImage result = this.currentImage;
		if (newIndex < this.index) {
			result = new BufferedImage(this.currentImage.getWidth(), this.currentImage.getHeight(),
					this.currentImage.getType());
		}

		for (int counter = newIndex; counter < this.index; counter++) {
			if (Thread.interrupted()) {
				return;
			}
			this.filter.addToImage(result, this.primitives.get(counter));
		}
		this.updateFrame(result, newIndex);
	}

	private void updateFrame(BufferedImage current, int newIndex) {
		this.currentImage = current;
		this.index = newIndex;
		this.notifyListeners(new ChangeEvent(this));
	}
}
