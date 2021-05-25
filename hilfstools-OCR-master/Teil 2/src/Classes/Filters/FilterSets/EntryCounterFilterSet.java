package Classes.Filters.FilterSets;

import Classes.Data.DataLine;
import Classes.Filters.FilterSet;
import Classes.Filters.Filters.EntryCounterFilter;

public class EntryCounterFilterSet extends FilterSet<DataLine> {
	public EntryCounterFilterSet() {
		this.add(new EntryCounterFilter());
	}
}