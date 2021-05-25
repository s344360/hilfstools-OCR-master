package GUI;

import Classes.Data.Collection;
import Classes.Data.StatTableEntry;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Comparator;

/**
 * Controller for MovePreview View.
 *
 * related class: GUI.MovePreview
 * FXML: GUI.MovePreview.fxml
 */
public class MovePreviewController {
	@FXML
	public Button btn_ok;

	// stats table head
	@FXML
	private TableView<StatTableEntry> table_stats_head;

	@FXML
	private TableColumn<StatTableEntry, String> table_stats_head_col_property;

	@FXML
	private TableColumn<StatTableEntry, StatTableEntry> table_stats_head_col_train;

	@FXML
	private TableColumn<StatTableEntry, StatTableEntry> table_stats_head_col_test;

	// stats table
	@FXML
	private TableView<StatTableEntry> table_stats;

	@FXML
	private TableColumn<StatTableEntry, String> table_stats_col_property;

	@FXML
	private TableColumn<StatTableEntry, StatTableEntry> table_stats_col_train;

	@FXML
	private TableColumn<StatTableEntry, StatTableEntry> table_stats_col_test;

	@FXML
	public void initialize() {
		// stats table head
		table_stats_head_col_property.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		table_stats_head_col_train.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		table_stats_head_col_test.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));

		table_stats_head_col_train.comparatorProperty().set(Comparator.comparingInt(a -> a.getResTrain().getValue()));
		table_stats_col_test.comparatorProperty().set(Comparator.comparingInt(a -> a.getResTest().getValue()));

		table_stats_head_col_train.setCellFactory(cellData -> new TableCell<StatTableEntry, StatTableEntry>() {
			@Override
			protected void updateItem(StatTableEntry s, boolean empty) {
				if(s == null)
					return;

				super.updateItem(s, empty);
				setText("" + s.getResTrain().getValue());
			}
		});

		table_stats_head_col_test.setCellFactory(cellData -> new TableCell<StatTableEntry, StatTableEntry>() {
			@Override
			protected void updateItem(StatTableEntry s, boolean empty) {
				if(s == null)
					return;

				super.updateItem(s, empty);
				setText("" + s.getResTest().getValue());
			}
		});


		// stats table
		table_stats_col_property.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));
		table_stats_col_train.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		table_stats_col_test.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));

		table_stats_col_train.comparatorProperty().set(Comparator.comparingInt(a -> a.getResTrain().getValue()));
		table_stats_col_test.comparatorProperty().set(Comparator.comparingInt(a -> a.getResTest().getValue()));

		table_stats_col_train.setCellFactory(cellData -> new TableCell<StatTableEntry, StatTableEntry>() {
			@Override
			protected void updateItem(StatTableEntry s, boolean empty) {
				if(s == null)
					return;

				super.updateItem(s, empty);
				setText("" + s.getResTrain().getValue());

				if(s.getResTrain().isSevere())
					setTextFill(Color.RED);
				else if(s.getResTrain().hasFailed())
					setTextFill(Color.ORANGE);
				else
					setTextFill(Color.BLACK);
			}
		});

		table_stats_col_test.setCellFactory(cellData -> new TableCell<StatTableEntry, StatTableEntry>() {
			@Override
			protected void updateItem(StatTableEntry s, boolean empty) {
				if(s == null)
					return;

				super.updateItem(s, empty);
				setText("" + s.getResTest().getValue());

				if(s.getResTest().isSevere())
					setTextFill(Color.RED);
				else if(s.getResTest().hasFailed())
					setTextFill(Color.ORANGE);
				else
					setTextFill(Color.BLACK);
			}
		});
	}

	/**
	 * Initialize UI data.
	 *
	 * @param statsLog Stats to show in the Stat Logs.
	 * @param referenceStatsLog The Stats Log entries of the original Stats Log.
	 */
	void initializeData(Collection<StatTableEntry> statsLog, Collection<StatTableEntry> referenceStatsLog) {
		Collection<StatTableEntry> statsHeadFiltered = new Collection<>();
		Collection<StatTableEntry> statsFiltered = new Collection<>();

		for(int i = 0; i < statsLog.size(); i++) {
			if(statsLog.get(i).getName().contains("Item") || statsLog.get(i).getName().length() > 2)
				statsHeadFiltered.add(statsLog.get(i));
			else if (statsLog.get(i).getResTrain().getValue() != referenceStatsLog.get(i).getResTrain().getValue())
				statsFiltered.add(statsLog.get(i));
		}

		table_stats_head.setItems(statsHeadFiltered.asGenericObservableList());
		table_stats.setItems(statsFiltered.asGenericObservableList());
	}

	/**
	 * Event, for clicking the "cancel" button.
	 */
	public void click_cancel() {
			((Stage) btn_ok.getScene().getWindow()).close();
	}
}
