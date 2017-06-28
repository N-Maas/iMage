package org.iMage.geometrify;

import java.awt.Point;

import org.iMage.geometrify.generators.RandomPointGenerator;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the RandomPointGenerator.
 * 
 * @author Nikolai
 */
public class RandomPointGeneratorTest {

	/**
	 * Tests whether randomly generated Points are not out of bounds.
	 */
	@Test
	public void generalTest() {
		RandomPointGenerator rpg = new RandomPointGenerator(10, 15);
		for (int i = 0; i < 100; i++) {
			Point p = rpg.nextPoint();
			Assert.assertTrue(p.x < 10);
			Assert.assertTrue(p.y < 15);
		}
	}
}