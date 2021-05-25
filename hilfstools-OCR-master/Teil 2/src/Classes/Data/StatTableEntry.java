package Classes.Data;

import Classes.Filters.FilterResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents a row in the StatsTable.
 */
public class StatTableEntry {
	private final FilterResult resTrain;
	private final FilterResult resTest;

	/**
	 * Creates a new StatTableEntry.
	 *
	 * @param resTrain FilterResult for trainSet-item
	 * @param resTest FilterResult for testSet-item
	 */
	public StatTableEntry(FilterResult resTrain, FilterResult resTest) {
		this.resTrain = resTrain;
		this.resTest = resTest;
	}

	/**
	 * Gets the label/name to be used for the row.
	 *
	 * @return Label/Name of the row.
	 */
	public String getName() {
		return this.resTrain.getName().length() > 0 ? this.resTrain.getName() : this.resTest.getName();
	}

	/**
	 * Gets the Property of the name.
	 *
	 * @return SimpleStringProperty of this.getName()
	 */
	public StringProperty nameProperty() {
		return new SimpleStringProperty(this.getName());
	}

	/**
	 * Gets the Train Set's FilterResult.
	 *
	 * @return FilterResult for the Train Set.
	 */
	public FilterResult getResTrain()
	{
		return this.resTrain;
	}

	/**
	 *  Gets the Test Set's FilterResult.
	 *
	 * @return FilterResult for the Test Set.
	 */
	public FilterResult getResTest()
	{
		return this.resTest;
	}
}
