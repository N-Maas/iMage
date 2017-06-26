package org.iMage.geometrify;

import java.awt.image.BufferedImage;

public interface PrimitivePictureFilter {
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples);
}
