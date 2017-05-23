package org.iMage.geometrify;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

/**
 * A triangle.
 *
 * @author Dominic Ziegler, Martin Blersch
 * @version 1.0
 */
public class Triangle implements IPrimitive {
	private final Polygon polygon;
	private Color color;

	/**
	 * Creates a new triangle from the given vertices.
	 *
	 * @param a
	 *            the first vertex
	 * @param b
	 *            the second vertex
	 * @param c
	 *            the third vertex
	 */
	public Triangle(Point a, Point b, Point c) {
		this.polygon = new Polygon();
		this.polygon.addPoint(a.x, a.y);
		this.polygon.addPoint(b.x, b.y);
		this.polygon.addPoint(c.x, c.y);
	}

	@Override
	public boolean isInsidePrimitive(Point p) {
		return this.polygon.contains(p);
	}

	@Override
	public BoundingBox getBoundingBox() {
		Rectangle bounds = this.polygon.getBounds();
		return new BoundingBox(new Point(bounds.x, bounds.y),
				new Point(bounds.x + bounds.width, bounds.y + bounds.height));
	}

	@Override
	public Color getColor() {
		if (this.color == null) {
			throw new IllegalStateException("Color is not set.");
		}
		return this.color;
	}

	@Override
	public void setColor(Color c) {
		this.color = c;
	}
}
