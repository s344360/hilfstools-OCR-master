package Classes.Data;

import Classes.Filters.FilterResult;
import Classes.Filters.IFilter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Generic Collection/DataSet.
 * Used as backing for DataSets as well as FilterSets.
 * 
 * @param <T> Type of the items in the Collection.
 */
public class Collection<T> extends LinkedList<T> {
	/**
	 * Creates a new Collection<T> from a given Collection<T>, only adding item accepted by the Predicate<T>.
	 *
	 * @param collection Collection<T> of items to add.
	 * @param pred Predicate to filter the items that are added from the collection.
	 */
	public Collection(List<T> collection, Predicate<T> pred) {
		this.add(collection, pred);
	}

	/**
	 * Creates a new, empty Collection<T>.
	 */
	public Collection() {
		// empty
	}

	@Override
	public boolean add(T item)
	{
		return item != null && super.add(item);
	}

	/**
	 * Adds items to the Collection<T> from a given Collection<T>, only adding item accepted by the Predicate<T>.
	 *
	 * @param collection Collection<T> of items to add.
	 * @param pred Predicate to filter the items that are added from the collection.
	 */
	public void add(List<T> collection, Predicate<T> pred) {
		for(T item : collection)
			if(pred.test(item))
				this.add(item);
	}

	/**
	 * Counts the number of items in the Collection<T> matching the Predicate pred.
	 *
	 * @param pred  Predicate to filter the items that are counted.
	 *
	 * @return number of items matching the Predicate pred.
	 */
	public int count(Predicate<T> pred)
	{
		int count = 0;

		for(T item : this)
			if(pred.test(item))
				count++;

		return count;
	}
	
	/**
	 * Applies a given filter to every element of the Collection.
	 * 
	 * @param filter Filter to apply.
	 * 
	 * @return List of exceptions thrown by the filter.
	 */
	public FilterResult applyFilter(IFilter<T> filter) {
		FilterResult message = new FilterResult();
		
		for(T entry : this)
			message = filter.mergeResults(message, filter.apply(entry));
		
		return message;
	}
	
	/**
	 * Gets the entries in the Collection as a ObservableList<String> for data binding.
	 * 
	 * @return ObservableList of DataLine entries.toString().
	 */
	public ObservableList<String> asObservableList() {
		LinkedList<String> list = new LinkedList<>();
		
		for(T entry : this)
			list.add(entry.toString());
		
		return FXCollections.observableArrayList(list);
	}
	
	/**
	 * Gets the entries in the Collection as a ObservableList<T> for data binding.
	 * 
	 * @return ObservableList of DataLine entries.
	 */
	public ObservableList<T> asGenericObservableList() {
		return FXCollections.observableArrayList(this);
	}
}