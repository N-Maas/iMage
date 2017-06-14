package filter;

public interface FilterObservable {

	public void addObserver(FilterObserver fo);

	public boolean deleteObserver(FilterObserver fo);
}
