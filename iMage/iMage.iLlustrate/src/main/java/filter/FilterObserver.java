package filter;

import java.awt.Image;

import org.iMage.geometrify.IPrimitive;

public interface FilterObserver {

	public void update(Image current, IPrimitive added);
}
