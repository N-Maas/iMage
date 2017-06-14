/**
 * 
 */
package org.iMage.geometrify;

import static org.junit.Assert.assertFalse;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RandomPointGenerator}
 * 
 * @author Tobias Hey
 *
 */
public class IPDRandomPointGeneratorTest {

	private RandomPointGenerator rpGenerator;
	private static final int WIDTH = 2;
	private static final int HEIGHT = 2;

	/**
	 * Sets up the test objects.
	 */
	@Before
	public void setUp() {
		rpGenerator = new RandomPointGenerator(WIDTH, HEIGHT);

	}

	/**
	 * Test method for {@link RandomPointGenerator#nextPoint()}
	 */
	@Test
	public void nextPointTest() {
		Point nextPoint;
		for (int i = 0; i < 1000; i++) {
			nextPoint = rpGenerator.nextPoint();

			assertFalse("Point is outside the allowed Range ( < 0)", nextPoint.getX() < 0 || nextPoint.getY() < 0);
			assertFalse("Point is outside the allowed Range ( >= width)", nextPoint.getX() >= WIDTH);
			assertFalse("Point is outside the allowed Range ( >= height)", nextPoint.getY() >= HEIGHT);

		}
	}

}
