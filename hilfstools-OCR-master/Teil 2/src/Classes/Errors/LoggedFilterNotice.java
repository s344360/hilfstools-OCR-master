package Classes.Errors;

public class LoggedFilterNotice extends LoggedException {
	/**
	 * Creates a new LoggedFilterNotice.
	 * 
	 * @param description Description text.
	 */
	public LoggedFilterNotice(String description) {
		super(description, LoggedExceptionType.FILTER);
	}
}