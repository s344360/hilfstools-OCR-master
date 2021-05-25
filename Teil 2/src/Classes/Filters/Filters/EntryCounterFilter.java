package Classes.Filters.Filters;

import Classes.Data.DataLine;
import Classes.Filters.FilterResult;
import Classes.Filters.IFilter;

public class EntryCounterFilter implements IFilter<DataLine> {
	@Override
	public FilterResult apply(DataLine item) {
		return new FilterResult("Items", "", 1);
	}
}