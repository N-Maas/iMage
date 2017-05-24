package org.iMage.geometrify;

import java.awt.Point;
import java.util.List;

@FunctionalInterface
public interface IPrimitive {
	public List<Point> getInsidePoints();
}
