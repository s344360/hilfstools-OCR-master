package Classes.Errors;

/**
 * 
 */
public class LoggedError extends LoggedException {
	/**
	 * Creates a new LoggedError.
	 * 
	 * @param description Description text.
	 */
	public LoggedError(String description) {
		super(description, LoggedExceptionType.ERROR);
	}
}