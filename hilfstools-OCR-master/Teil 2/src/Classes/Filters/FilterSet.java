package Classes.Filters;

import Classes.Data.Collection;

import java.util.LinkedList;

/**
 * Represents a set of Filters.
 * A Set of Filters may be applied to an <T>-Item or Collection<T> collectively.
 *
 * @param <T> Type the Filter should be applied to.
 */
public class FilterSet<T> {
	private LinkedList<IFilter<T>> filters = new LinkedList<>();
	private boolean isEnabled = true;
	
	/**
	 * Adds a Filter to the Set.
	 * 
	 * @param filter Filter to add.
	 */
	public void add(IFilter<T> filter) {
		this.filters.add(filter);
	}

	/**
	 * Checks, if the FilterSet is enabled.
	 *
	 * @return true: FilterSet enabled; false: otherwise
	 */
	public boolean isEnabled() { return this.isEnabled; }

	/**
	 * Sets the FilterSet as enabled.
	 */
	public void enable() {
		this.isEnabled = true;
	}

	/**
	 *  Sets the FilterSet as disabled.
	 */
	public void disable() {
		this.isEnabled = false;
	}

//	/**
//	 * Applies all the Filters in the Set to a given item.
//	 *
//	 * @param item Item to apply the filters to.
//	 * @return List of the Filters' results.
//	 */
//	public LinkedList<FilterResult> apply(T item) {
//		LinkedList<FilterResult> results = new LinkedList<>();
//
//		if(this.isEnabled)
//			for(IFilter<T> filter : this.filters)
//				results.add(filter.apply(item));
//
//		return results;
//	}
	
	/**
	 * Applies all the Filters in the Set to a given Collection<T>.
	 * 
	 * @param items Collection to apply the filters to.
	 * @return List of the Filters' results.
	 */
	public LinkedList<FilterResult> apply(Collection<T> items) {
		LinkedList<FilterResult> results = new LinkedList<>();
		
		if(this.isEnabled)
			for(IFilter<T> filter : this.filters)
				results.add(items.applyFilter(filter));
		
		return results;
	}
}