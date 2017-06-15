package filter;

/**
 * An Observable that can be observed by FilterObservers. It should update them
 * with all added IPrimitives and the current version of the calculated image.
 * 
 * @author Nikolai
 */
public interface FilterObservable {

	/**
	 * Adds an observer.
	 * 
	 * @param fo
	 *            observer to be added
	 */
	void addObserver(FilterObserver fo);

	/**
	 * Deletes an observer.
	 * 
	 * @param fo
	 *            observer to be deleted
	 * @return true, if the observer was contained
	 */
	boolean deleteObserver(FilterObserver fo);
}
