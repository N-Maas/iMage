package filter;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.iMage.geometrify.RandomPointGenerator;
import org.iMage.geometrify.TrianglePictureFilter;
import org.junit.Test;

public class ObservableTPFilterTest {

	/**
	 * Complex test case for
	 * {@link TrianglePictureFilter#apply(BufferedImage, int, int)}.
	 *
	 * @throws IOException
	 *             if some I/O operation failed
	 */
	@Test
	public void testObserver() throws IOException {
		final int numberOfIterations = 10;
		final int numberOfSamples = 10;
		int[] count = { 0 };

		BufferedImage test = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		ObservableTPFilter filter = new ObservableTPFilter(new RandomPointGenerator(test.getWidth(), test.getHeight()));
		filter.addObserver((a, b) -> count[0]++);
		filter.addObserver((a, b) -> count[0] -= 2);
		BufferedImage result = filter.apply(test, numberOfIterations, numberOfSamples);
		assertEquals(-10, count[0]);
	}
}
