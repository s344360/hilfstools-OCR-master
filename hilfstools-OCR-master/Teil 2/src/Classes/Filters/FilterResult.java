package Classes.Filters;

import Classes.Errors.LoggedException;
import Classes.Errors.LoggedExceptionType;
import Classes.Errors.LoggedFilterNotice;
import Classes.Errors.LoggedFilterSevereNotice;

/**
 * Represents the Result of a Filter.
 */
public class FilterResult {
	private String name = "";
	private int value = 0;
	private String description = "";
	private boolean hasFailed = false;
	private LoggedExceptionType exceptionType = LoggedExceptionType.FILTER;
	
	/**
	 * 
	 * @param name Name of the Filter/FilterResult.
	 * @param description Description to show in the error box.
	 * @param value Value
	 */
	public FilterResult(String name, String description, int value) {
		this.name = name;
		this.description = description;
		this.value = value;
	}
	
	/**
	 * New, empty FilterResult. 
	 */
	public FilterResult() {
	}

	public FilterResult setSevere() {
		this.exceptionType = LoggedExceptionType.FILTER_SEVERE;
		return this;
	}

	public boolean hasFailed() {
		return this.hasFailed;
	}

	public boolean isSevere() {
		return this.exceptionType == LoggedExceptionType.FILTER_SEVERE;
	}
	
	public FilterResult setFailed() {
		this.hasFailed = true;
		return this;
	}

	public FilterResult setPassed() {
		this.hasFailed = false;
		return this;
	}

	public LoggedExceptionType getExceptionType()
	{
		return this.exceptionType;
	}
	
	public String getDescription() {
		return this.description;
	}

	/**
	 * Gets the name of the FilterResult.
	 *
	 * @return name of the FilterResult
	 */
	public String getName() {
		return this.name;
	}
	
	public int getValue() {
		return this.value;
	}

	/**
	 * Get's the FilterResult as a LoggedException for logging.
	 *
	 * @param format Format of the LoggedException's description.
	 *               Templating Tags:
	 *               %name% = FilterResult.getName()
	 *               %value% = FilterResult.getValue()
	 *               %decription% = FilterResult.getDescription()
	 *
	 *               e.g. "%name% returned value %value%."
	 *
	 * @return formatted LoggedException
	 */
	public LoggedException getFilterNotice(String format) {
		format = format.replace("%value%", "" + this.getValue());
		format = format.replace("%name%", this.getName());
		format = format.replace("%description%", "" + this.getDescription());

		if(this.exceptionType == LoggedExceptionType.FILTER_SEVERE)
			return new LoggedFilterSevereNotice(format);

		return new LoggedFilterNotice(format);
	}

//	/**
//	 * Get's the FilterResult as a LoggedException for logging, the the FilterResult is marked as failed.
//	 * If the FilterResult is not marked as failed, null is returned.
//	 *
//	 * @param format Format of the LoggedException's description.
//	 *               Templating Tags:
//	 *               %name% = FilterResult.getName()
//	 *               %value% = FilterResult.getValue()
//	 *               %decription% = FilterResult.getDescription()
//	 *
//	 *               e.g. "%name% returned value %value%."
//	 *
//	 * @return formatted LoggedException or null
//	 */
//	public LoggedException getFilterNoticeIfFailed(String format) {
//		if(this.hasFailed)
//			return this.getFilterNotice(format);
//
//		return null;
//	}
}