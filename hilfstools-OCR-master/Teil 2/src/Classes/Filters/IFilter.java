package Classes.Filters;


/**
 * Base Class Interface for all Filters.
 *
 * @param <T> Type the Filter should be applied to.
 */
public interface IFilter<T> {
	/**
	 * Filter function to apply to the item.
	 * 
	 * @param item Item to apply the filter function to.
	 *
	 * @return FilterResult from the Filter
	 */
	FilterResult apply(T item);
	
	/**
	 * Defines how the new and old result are merged when executing a FilterSet.
	 * Only override if really necessary.
	 * 
	 * NOTE: failed FilterResults are passed down without being merged.
	 * 
	 * @param res1 Old FilterResult.
	 * @param res2 New FilterResult.
	 * @return Merged FilterResult.
	 */
	default FilterResult mergeResults(FilterResult res1, FilterResult res2) {
		if(res1.hasFailed())
			return res1;
		
		if(res2.hasFailed())
			return res2;
		
		return new FilterResult(res2.getName(), res2.getDescription(), res1.getValue() + res2.getValue());
	}
}