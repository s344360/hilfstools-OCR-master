package GUI;

import Classes.Data.DataLineType;
import Classes.Data.ForceTableEntry;
import Settings.Settings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

/**
 * Controller for Settings View.
 *
 * related class: GUI.Settings
 * FXML: GUI.Settings.fxml
 */
public class SettingsController {
	private int itemCount = 0;
	private boolean blockNumEvents = false;

	// Global
	@FXML
	public Button btn_ok;

	// TAB: Distribution & Highlighting
	@FXML
	private Slider distribution_slider_simple;

	@FXML
	private Label distribution_lbl_simple_train;

	@FXML
	private Spinner num_train;

	@FXML
	private Spinner num_test;

	@FXML
	private Label distribution_lbl_simple_test;

	@FXML
	private CheckBox distribution_cb_character_compensation;

	@FXML
	private ListView<ForceTableEntry> list_force_train;

	// Thresholds
	@FXML
	private TextField txt_highlight_critical_values_notice_threshold;

	@FXML
	private TextField txt_highlight_critical_values_warning_threshold;

	// TAB: Statistics
	@FXML
	private CheckBox stats_cb_letter_counts;

	@FXML
	private CheckBox stats_cb_type_counts;

	@FXML
	private CheckBox stats_cb_item_counts;


	@FXML
	void initialize() {
		list_force_train.setCellFactory(CheckBoxListCell.forListView(ForceTableEntry::checkedProperty));

		initLambdaEvents();
		Settings settings = Settings.getInstance();

		distribution_slider_simple.setValue(100 * settings.simpleDistributionTrainPercentage);

		distribution_cb_character_compensation.setSelected(settings.useCharacterCompensation);

		stats_cb_item_counts.setSelected(settings.dataFilters.get("entryCounter").isEnabled());
		stats_cb_type_counts.setSelected(settings.dataFilters.get("typeCounter").isEnabled());
		stats_cb_letter_counts.setSelected(settings.dataFilters.get("letterCounter").isEnabled());

		txt_highlight_critical_values_notice_threshold.setText("" + settings.highlightCriticalValuesNoticeThreshold);
		txt_highlight_critical_values_warning_threshold.setText("" + settings.highlightCriticalValuesWarningThreshold);
	}

	/**
	 * Initialize Data in UI.
	 *
	 * @param forceTableEntries List of entries for the "Force 100% Train" table.
	 * @param itemCount The number of items in the loaded data set.
	 */
	void initData(List<ForceTableEntry> forceTableEntries, int itemCount) {
		Settings settings = Settings.getInstance();

		this.itemCount = itemCount;
		list_force_train.getItems().addAll(forceTableEntries);

		this.blockNumEvents = true;
		num_train.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, this.itemCount, (int) (this.itemCount * settings.simpleDistributionTrainPercentage)));
		num_test.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, this.itemCount, (int) (this.itemCount * (1 - settings.simpleDistributionTrainPercentage))));
		this.blockNumEvents = false;
	}

	/**
	 * Initialize Events defined by Lambdas.
	 */
	private void initLambdaEvents() {
		distribution_slider_simple.valueProperty().addListener((observable, oldValue, newValue) -> {
			distribution_lbl_simple_train.setText(newValue.intValue() + " %");
			distribution_lbl_simple_test.setText((100 - newValue.intValue()) + " %");

			if(!this.blockNumEvents) {
				if(num_train.getValueFactory() == null)
					return;

				num_train.getValueFactory().setValue((int) (this.itemCount * (newValue.intValue()) / 100f));
				num_test.getValueFactory().setValue((int) (this.itemCount * ((100 - newValue.intValue()) / 100f)));
			}
		});

		num_train.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(!this.blockNumEvents) {
				this.blockNumEvents = true;

				if (this.itemCount > 0)
					distribution_slider_simple.setValue(100 * Integer.parseInt(newValue.toString()) / (float) this.itemCount);

				num_test.getValueFactory().setValue(this.itemCount - Integer.parseInt(newValue.toString()));

				this.blockNumEvents = false;
			}
		});

		num_test.valueProperty().addListener((observable, oldValue, newValue) -> {
			if(!this.blockNumEvents) {
				this.blockNumEvents = true;

				if (this.itemCount > 0)
					distribution_slider_simple.setValue(100 * (1 - Integer.parseInt(newValue.toString()) / (float) this.itemCount));

				num_train.getValueFactory().setValue(this.itemCount - Integer.parseInt(newValue.toString()));

				this.blockNumEvents = false;
			}
		});
	}

	/**
	 * Event for clicking "ok" -button.
	 * Saves settings to Settings.Settings before exiting the Window.
	 */
	@FXML
	public void click_ok() {
		Settings settings = Settings.getInstance();

		// File & Distribution
		settings.simpleDistributionTrainPercentage = (float) distribution_slider_simple.getValue() / 100f;
		settings.useCharacterCompensation = distribution_cb_character_compensation.isSelected();

		settings.forceFullTrain.clear();
		for (ForceTableEntry entry : list_force_train.getItems())
			if(entry.getChecked())
				settings.forceFullTrain.add(entry.getDataLineType());

		// Satistics (Filters)
		settings.dataFilters.get("entryCounter").disable();
		settings.dataFilters.get("typeCounter").disable();
		settings.dataFilters.get("letterCounter").disable();

		if(stats_cb_item_counts.isSelected())
			settings.dataFilters.get("entryCounter").enable();

		if(stats_cb_type_counts.isSelected())
			settings.dataFilters.get("typeCounter").enable();

		if(stats_cb_letter_counts.isSelected())
			settings.dataFilters.get("letterCounter").enable();

		// Messages & Warnings
		settings.highlightCriticalValuesNoticeThreshold = Integer.parseInt(txt_highlight_critical_values_notice_threshold.getText());
		settings.highlightCriticalValuesWarningThreshold = Integer.parseInt(txt_highlight_critical_values_warning_threshold.getText());

		click_cancel();
	}

	/**
	 * Event for clicking "cancel" -button.
	 */
	@FXML
	private void click_cancel() {
		((Stage) btn_ok.getScene().getWindow()).close();
	}
}