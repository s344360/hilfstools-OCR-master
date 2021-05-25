package Classes.Errors;

public class LoggedInfo extends LoggedException {
	/**
	 * Creates a new LoggedInfo.
	 *
	 * @param description Description text.
	 */
	public LoggedInfo(String description) {
		super(description, LoggedExceptionType.INFO);
	}
}