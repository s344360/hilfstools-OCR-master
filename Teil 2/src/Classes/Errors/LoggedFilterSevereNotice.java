package Classes.Errors;

public class LoggedFilterSevereNotice extends LoggedException {
	/**
	 * Creates a new LoggedFilterNotice.
	 *
	 * @param description Description text.
	 */
	public LoggedFilterSevereNotice(String description) {
		super(description, LoggedExceptionType.FILTER_SEVERE);
	}
}