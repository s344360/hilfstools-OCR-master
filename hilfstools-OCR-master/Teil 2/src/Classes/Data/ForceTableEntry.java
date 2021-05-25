package Classes.Data;

import javafx.beans.property.SimpleBooleanProperty;

/**
 * Represents a row in the Settings "Force 100% Train"-list.
 */
public class ForceTableEntry {
	private final DataLineType dataLineType;
	private final int count;
	private final SimpleBooleanProperty checked = new SimpleBooleanProperty();

	/**
	 * Creates a new ForceTableEntry.
	 *
	 * @param dataLineType DataLineType represented by the ForceTableEntry.
	 * @param count Count of how often the given DataLineType is contained in the Data loaded.
	 * @param checked Determines, if the entry is checked/selected in the list.
	 */
	public ForceTableEntry(DataLineType dataLineType, int count, boolean checked) {
		this.dataLineType = dataLineType;
		this.count = count;
		setChecked(checked);
	}

	/**
	 * Gets the DataLineType of the ForceTableEntry.
	 *
	 * @return DataLineType of the ForceTableEntry
	 */
	public DataLineType getDataLineType() {
		return this.dataLineType;
	}

	/**
	 * Property of the checked state.count
	 * Required for UI interaction.
	 *
	 * @return Checked state SimpleBooleanProperty.
	 */
	public SimpleBooleanProperty checkedProperty()
	{
		return this.checked;
	}

	/**
	 * Gets the checked-state.
	 *
	 * @return checked state
	 */
	public boolean getChecked() {
		return this.checkedProperty().get();
	}

	private void setChecked(boolean checked) {
		this.checkedProperty().set(checked);
	}

	/**
	 * Gets the Label/Name for the ForceTableEntry.
	 *
	 * @return Name of the Entry (this.dataLineType.toStringLong())
	 */
	public String toString()
	{
		return this.dataLineType.toStringLong() + " (" + this.count + " items)";
	}
}
