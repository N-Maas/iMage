package org.iMage.geometrify;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;

public class RandomPointGeneratorTest {

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