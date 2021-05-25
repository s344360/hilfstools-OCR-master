package Classes.Filters.Filters;

import Classes.Data.DataLine;
import Classes.Filters.FilterResult;
import Classes.Filters.IFilter;
import Settings.Settings;

public class LetterCounterFilter implements IFilter<DataLine> {
	private String checkChar = "A";
	
	public LetterCounterFilter(String checkChar) {
		this.checkChar = checkChar;
	}

	@Override
	public FilterResult apply(DataLine item) {
		String txt = item.getText(); 
		int count = txt.length() - txt.replace(this.checkChar, "").length();
		
		return new FilterResult(this.checkChar, "", count).setFailed();
	}

	@Override
	public FilterResult mergeResults(FilterResult res1, FilterResult res2) {
		Settings settings = Settings.getInstance();
		FilterResult cumResult = new FilterResult(res2.getName(), res2.getDescription(), res1.getValue() + res2.getValue());

		if(cumResult.getValue() < settings.highlightCriticalValuesWarningThreshold)
			return cumResult.setFailed().setSevere();

		if(cumResult.getValue() < settings.highlightCriticalValuesNoticeThreshold)
			return cumResult.setFailed();

		return cumResult.setPassed();
	}
}