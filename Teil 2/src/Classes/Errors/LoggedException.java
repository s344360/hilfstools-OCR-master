package Classes.Errors;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Base Class for Exceptions/Messages that will be shown in Error Log in the GUI.
 */
public abstract class LoggedException extends Exception {
	private LoggedExceptionType type;
	
	/**
	 * Creates a new LoggedException.
	 * 
	 * @param description Description text.
	 * @param type Type of the Exception/Message.
	 */
	LoggedException(String description, LoggedExceptionType type) {
		super(description);
		this.type = type;
	}
	
	public LoggedExceptionType getType() {
		return this.type;
	}
	
	public StringProperty typeProperty() {
		return new SimpleStringProperty(this.type.toString());
	}
	
//	public StringProperty messageProperty() {
//		return new SimpleStringProperty(this.getMessage());
//	}
}