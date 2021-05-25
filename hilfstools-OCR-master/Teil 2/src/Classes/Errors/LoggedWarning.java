package Classes.Errors;

public class LoggedWarning extends LoggedException {
	/**
	 * Creates a new LoggedWarning.
	 * 
	 * @param description Description text.
	 */
	public LoggedWarning(String description) {
		super(description, LoggedExceptionType.WARNING);
	}
}