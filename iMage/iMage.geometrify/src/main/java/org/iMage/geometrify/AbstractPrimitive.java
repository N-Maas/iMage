package org.iMage.geometrify;

public abstract class AbstractPrimitive implements IPrimitive {
	private int[] insidePoints;

	/**
	 * Creates a list of all points that are inside the specified primitive. The
	 * calculation is cached and defined by the subclasses.
	 * 
	 * @param ip
	 *            the primitive
	 * @return list of all points inside the primitive
	 */
	@Override
	public int[] getInsidePoints() {
		if (this.insidePoints == null) {
			this.insidePoints = this.calculatePoints();
		}
		return this.insidePoints;
	}

	protected abstract int[] calculatePoints();
}
