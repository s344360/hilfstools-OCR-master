package Classes.Filters.Filters;

import Classes.Data.DataLine;
import Classes.Data.DataLineType;
import Classes.Filters.FilterResult;
import Classes.Filters.IFilter;

public class TypeCounterFilter implements IFilter<DataLine> {
	private DataLineType checkType = DataLineType.PARAGRAPH;

	public TypeCounterFilter(DataLineType checkType) {
		this.checkType = checkType;
	}

	@Override
	public FilterResult apply(DataLine item) {
		return new FilterResult(this.checkType.toStringLong(), "", (item.getType() == this.checkType ? 1 : 0));
	}
}