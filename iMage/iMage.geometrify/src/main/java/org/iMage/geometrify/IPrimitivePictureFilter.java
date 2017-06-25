package org.iMage.geometrify;

import java.awt.image.BufferedImage;

public interface IPrimitivePictureFilter {
	public BufferedImage apply(BufferedImage image, int numberOfIterations, int numberOfSamples);
}
