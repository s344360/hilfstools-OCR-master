package Classes.Errors;

/**
 * Specifies the type of an LoggedException.
 *
 * FILTER, FILTER_SEVERE are intended to be shown in Stats Logs, WARNING and ERROR types are to be shown in Error Log.
 * 
 * Types:
 * FILTER: Filter Error Messages
 * FILTER_SEVERE: Filter Error Messages (severe = red on log)
 * INFO: Information notice
 * WARNING: Input Errors
 * ERROR: Java Exceptions
 */
public enum LoggedExceptionType {
	FILTER("Filter"),
	FILTER_SEVERE("Filter"),
	INFO("Info"),
	WARNING("Warning"),
	ERROR("Error");
	
	private String niceName;
	
	LoggedExceptionType(String niceName) {
        this.niceName = niceName;
    }
	
	@Override
	public String toString() {
		return this.niceName;
	}
}