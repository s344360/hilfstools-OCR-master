package GUI;

import Classes.Data.*;
import Classes.Data.Collection;
import Classes.Distributions.SimpleDistribution;
import Classes.Errors.*;
import Classes.Filters.FilterResult;
import Classes.Filters.FilterSet;
import Classes.Filters.Filters.LetterCounterFilter;
import Classes.Filters.Filters.TypeCounterFilter;
import Settings.Settings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * Controller for Main View.
 *
 * related class: GUI.Main
 * FXML: GUI.Main.fxml
 */
public class MainController {
	private Collection<DataLine> trainSet = new Collection<>();
	private Collection<DataLine> trainListFiltered;
	private Collection<DataLine> testSet = new Collection<>();
	private Collection<DataLine> testListFiltered;

	private Predicate<DataLine> viewFilter = i -> true;

	private Collection<StatTableEntry> statsLog = new Collection<>();
	private Collection<LoggedException> errorLog = new Collection<>();

	private List<DataLineType> availableTypes = new LinkedList<>();

	// Menu
	@FXML
	private Menu menu_settings;

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

	// error message log
	@FXML
	private TableView<LoggedException> table_errors;

    @FXML
	private TableColumn<LoggedException, String> table_errors_col_type;
    
    @FXML
	private TableColumn<LoggedException, LoggedException> table_errors_col_description;

    // test / train lists
    @FXML
    private ListView<String> list_train;

    @FXML
    private ListView<String> list_test;

    // Details
	@FXML
	private Label lbl_details_text;

	@FXML
	private Label lbl_details_type;
	
	@FXML
	private ImageView imv_image;

	@FXML
	public Button btn_move_to_train;

	@FXML
	public Button btn_move_to_test;

    @FXML
    void initialize() {
    	// Train / Test lists
    	list_train.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	list_test.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    	// Error table
    	table_errors_col_type.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
		table_errors_col_description.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));

    	table_errors_col_description.setCellFactory(cellData -> new TableCell<LoggedException, LoggedException>() {
			@Override
			protected void updateItem(LoggedException s, boolean empty) {
				if(s == null)
					return;

				super.updateItem(s, empty);

				setText(s.getMessage());

				if(s.getType() == LoggedExceptionType.FILTER_SEVERE || s.getType() == LoggedExceptionType.ERROR)
					setTextFill(Color.RED);
				if(s.getType() == LoggedExceptionType.INFO)
					setTextFill(Color.GREEN);
				else
					setTextFill(Color.BLACK);
			}
		});

		// Stats table head
		table_stats_head.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_stats_head.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_stats_head_col_property.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		table_stats_head_col_train.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
		table_stats_head_col_test.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));

		table_stats_head_col_train.comparatorProperty().set(Comparator.comparingInt(a -> a.getResTrain().getValue()));
		table_stats_head_col_test.comparatorProperty().set(Comparator.comparingInt(a -> a.getResTest().getValue()));

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

    	// Stats table
		table_stats.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_stats.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_stats_col_property.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
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

		initLambdaEvents();
        updateUI();
    }

	/**
	 * Initialize Events defined by Lambdas.
	 */
	private void initLambdaEvents()
	{
		list_train.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				if(list_train.getSelectionModel().getSelectedIndex() > -1)
					showDetails(trainListFiltered.get(list_train.getSelectionModel().getSelectedIndex()));
			}
		);

		list_test.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
				if(list_test.getSelectionModel().getSelectedIndex() > -1)
					showDetails(testListFiltered.get(list_test.getSelectionModel().getSelectedIndex()));
			}
		);

		table_stats_head.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
			viewFilter = i -> true;

			if(table_stats_head.getSelectionModel().getSelectedIndices().size() > 0) {
				viewFilter = i -> false;
				ObservableList<StatTableEntry> selectedEntries = table_stats_head.getSelectionModel().getSelectedItems();

				for (StatTableEntry entry : selectedEntries) {
					if (entry.getName().contains("Items"))
						viewFilter = i -> true;
					else
						viewFilter = viewFilter.or(i -> i.getType().toStringLong().equals(entry.getName()));
				}
			}

			updateUISets();
		});

		table_stats.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
			viewFilter = i -> true;

			if(table_stats.getSelectionModel().getSelectedIndices().size() > 0) {
				viewFilter = i -> false;
				ObservableList<StatTableEntry> selectedEntries = table_stats.getSelectionModel().getSelectedItems();

				for (StatTableEntry entry : selectedEntries)
					viewFilter = viewFilter.or(i -> i.getText().contains(entry.getName()));
			}

			updateUISets();
		});
	}

	/**
	 * Shows the detail view for a given DataLine object.
	 *
	 * @param dataLine DataLine to show the details for.
	 */
	private void showDetails(DataLine dataLine)
	{
		lbl_details_text.setText(dataLine.getText());
		lbl_details_type.setText("(" + dataLine.getType().toStringLong() + ")");
		
		imv_image.setImage(dataLine.getImage());
		imv_image.autosize();
	}

	/**
	 * Applies all active FilterSets to the current data-Sets.
	 * (Does not refresh the UI!)
	 */
	private void applyFilters() {
    	this.errorLog.clear();
    	this.statsLog.clear();

		Collection<FilterResult> trainSetErrorLog = new Collection<>();
		Collection<FilterResult> testSetErrorLog = new Collection<>();

    	for(Map.Entry<String, FilterSet<DataLine>> entry : Settings.getInstance().dataFilters.entrySet()) {
    		// apply to test set
    	    LinkedList<FilterResult> res = entry.getValue().apply(trainSet);
			trainSetErrorLog.addAll(res);
    	    
    	    // apply to train set
    	    res = entry.getValue().apply(testSet);
			testSetErrorLog.addAll(res);
    	}

    	for(int i = 0; i < trainSetErrorLog.size(); i++)
			if(trainSetErrorLog.get(i).getValue() + testSetErrorLog.get(i).getValue() > 0)
				statsLog.add(new StatTableEntry(trainSetErrorLog.get(i), testSetErrorLog.get(i)));
	}

	/**
	 * Updates the GUI (set views + logs).
	 * Call after editing any of the Sets or Logs.
	 */
	private void updateUI() {
		updateUISets();
    	
    	table_errors.setItems(errorLog.asGenericObservableList());
		table_stats_head.setItems(new Collection<StatTableEntry>(statsLog, i -> i.getName().length() != 1).asGenericObservableList());
		table_stats.setItems(new Collection<StatTableEntry>(statsLog, i -> i.getName().length() == 1).asGenericObservableList());
	}

	/**
	 * Updates the GUI (set views only).
	 * Call after editing any of the Sets.
	 */
	private void updateUISets() {
		trainListFiltered = new Collection<>(trainSet, viewFilter);
		list_train.itemsProperty().bind(new SimpleListProperty<>(trainListFiltered.asObservableList()));

		testListFiltered = new Collection<>(testSet, viewFilter);
		list_test.itemsProperty().bind(new SimpleListProperty<>(testListFiltered.asObservableList()));
	}



 
    // UI EVENTS ------------------------------------------------------------------------------------

	/**
	 * Shows the MovePreview View and if positive confirm, moves the items to the Train/Test Sets.
	 *
	 * @param trainSetPreview new Train Set
	 * @param testSetPreview new Test Set
	 */
	private void showMoveConfirm(Collection<DataLine> trainSetPreview, Collection<DataLine> testSetPreview) {
		try {
			// get stats
			Collection<StatTableEntry> stats = new Collection<>();
			Collection<FilterResult> trainSetErrorLog = new Collection<>();
			Collection<FilterResult> testSetErrorLog = new Collection<>();

			for(Map.Entry<String, FilterSet<DataLine>> entry : Settings.getInstance().dataFilters.entrySet()) {
				// apply to test set
				trainSetErrorLog.addAll(entry.getValue().apply(trainSetPreview));

				// apply to train set
				testSetErrorLog.addAll(entry.getValue().apply(testSetPreview));
			}

			for(int i = 0; i < trainSetErrorLog.size(); i++)
				stats.add(new StatTableEntry(trainSetErrorLog.get(i), testSetErrorLog.get(i)));

			// show form
			MovePreview mp = new MovePreview();

			if(mp.show(stats, this.statsLog, (Stage) btn_move_to_train.getScene().getWindow())) {
				trainSet = trainSetPreview;
				testSet = testSetPreview;

				applyFilters();
				updateUI();
			}

		} catch (Exception ex) {
			errorLog.add(new LoggedError("Couldn't open MovePreview: " + ex.getMessage()));
		}
	}

	/**
	 * Event for clicking the "<-"-button.
	 * Moves the selected items to Train Set.
	 */
    public void click_move_to_train() {
    	if(list_test.getSelectionModel().getSelectedIndices().size() <= 0)
			return;

    	Collection<DataLine> trainSetPreview = new Collection<>(trainSet, i -> true);
    	Collection<DataLine> testSetPreview = new Collection<>(testSet, i -> true);
		ObservableList<Integer> indices = list_test.getSelectionModel().getSelectedIndices();

		for(int i = indices.size() - 1; i >= 0; i--) {
			DataLine selectedItem = testListFiltered.get(indices.get(i));
			trainSetPreview.add(selectedItem);
			testSetPreview.remove(selectedItem);
		}

		showMoveConfirm(trainSetPreview, testSetPreview);
	}

	/**
	 * Event for clicking the "->"-button.
	 * Moves the selected items to Test Set.
	 */
	public void click_move_to_test() {
		if(list_train.getSelectionModel().getSelectedIndices().size() <= 0)
			return;

		Collection<DataLine> trainSetPreview = new Collection<>(trainSet, i -> true);
		Collection<DataLine> testSetPreview = new Collection<>(testSet, i -> true);
		ObservableList<Integer> indices = list_train.getSelectionModel().getSelectedIndices();

		for(int i = indices.size() - 1; i >= 0; i--) {
			DataLine selectedItem = trainListFiltered.get(indices.get(i));
			testSetPreview.add(selectedItem);
			trainSetPreview.remove(selectedItem);
		}

		showMoveConfirm(trainSetPreview, testSetPreview);
	}

    
    // Menu ------------------------------------------------------------------------------------------

	/**
	 * Event for clicking the "save"-button in Menu "File"
	 *
	 * @throws FileNotFoundException Throws FileNotFoundExceptionif trying to open file that doesn't exist.
	 */
	public void click_menu_file_open() throws FileNotFoundException {
    	DirectoryChooser chooser = new DirectoryChooser();
    	chooser.setTitle("Choose Directory with Datalines");
    	File folder = chooser.showDialog(null);

    	if (folder == null)
    		return;

    	ArrayList<File> files = new ArrayList<>(Arrays.asList(folder.listFiles()));
    	Collection<DataLine> fullSet = init_data_set(files);

    	afterLoadInit(fullSet);

		SimpleDistribution dist = new SimpleDistribution();
		dist.performDistribution(fullSet, trainSet, testSet);

		applyFilters();
    	updateUI();
    }

	/**
	 * Event for clicking the "save"-button in Menu "File".
	 */
	@FXML
	public void click_menu_file_save() {
		errorLog.clear();

		// Create Directories
		DataLine d;
		if (!trainSet.isEmpty())
			d = trainSet.getFirst();
		else if (!testSet.isEmpty())
			d = trainSet.getFirst();
		else
			return;

		String testDirString = d.getImageFile().getParent() + "\\..\\Test";
		String trainDirString = d.getImageFile().getParent() + "\\..\\Train";
		File testDir = new File(testDirString);
		File trainDir = new File(trainDirString);

		if(!trainDir.mkdir() && !trainDir.exists())
			errorLog.add(new LoggedError("Couldn't create Train-Directory."));

		if(!testDir.mkdir() && !testDir.exists())
			errorLog.add(new LoggedError("Couldn't create Test-Directory."));

		// Copy Files
		if (!copy_files(trainSet, trainDirString))
			errorLog.add(new LoggedError("Error Copying Train files!"));
		else if (!copy_files(testSet, testDirString))
			errorLog.add(new LoggedError("Error Copying Test files!"));
		else
			errorLog.add(new LoggedInfo("Data saved successfully."));

		updateUI();
    }

	/**
	 * Event for clicking the "show"-button in Menu "Settings".
	 * Shows the Settings View.
	 */
	@FXML
	public void click_menu_settings_show() {
		Settings settings = Settings.getInstance();
		List<ForceTableEntry> forceTableEntries = new LinkedList<>();

		for(DataLineType type : availableTypes) {
			int count = trainSet.count(i -> i.getType() == type) + testSet.count(i -> i.getType() == type);
			forceTableEntries.add(new ForceTableEntry(type, count, settings.forceFullTrain.contains(type)));
		}

		GUI.Settings settingsUI = new GUI.Settings();

		if(settingsUI.show(forceTableEntries, trainSet.size() + testSet.size(), (Stage) btn_move_to_train.getScene().getWindow()))
			updateUI();
	}

	/**
	 * Recloses the Settings Menu item to achieve a menu-head-click effect.
	 */
	public void click_menu_settings_showing() {
		this.menu_settings.hide();
	}

	// Helper ------------------------------------------------------------------------------

	/**
	 * Returns the the String without extension (last found ".")
	 * @param path String of which the extension is cut
	 * @return path without extension
	 */
	private String removeExt(String path) {
		int point_index = path.lastIndexOf(".");

		return path.substring(0, point_index);
	}

	/** Checks all Files for two of the same name without double extentions, meaning a1.gt.txt == a1.bin.png and initializes 
	 * the DataLines for the new Set
	 *
	 * @param files List containing the Files to be added in a Collection. If a File is a Directory it will be skipped
	 * @return a Collection of DataLines representing the input Files
	 */
    private Collection<DataLine> init_data_set(ArrayList<File> files) {
    	ArrayList<File> resFiles = new ArrayList<>();
    	while (!files.isEmpty()) {
    		File f = files.remove(0);
    		if (!f.isFile()) continue;
    		String baseF = removeExt(removeExt(f.getName()));
    		for (File check : files) {
    			if (!check.isFile()) continue;
    			String baseC = removeExt(removeExt(check.getName()));
        		if (baseF.equals(baseC)) {
        			resFiles.add(f);
        			resFiles.add(check);
        			files.remove(check);
        			break;
        		}
    		}
    	}

    	Collection<DataLine> resCol = new Collection<>();
    	int halfSize = resFiles.size() / 2;
    	for (int i = 0;i<halfSize;i++) {
    		File f1 = resFiles.remove(0);
    		File f2 = resFiles.remove(0);
    		DataLineType type = getType(f1.getName());
    		String text;
    		if (f1.getName().endsWith("txt")) {
    			try {
					text = FileUtils.readFileToString(f1, "UTF-8");
					resCol.add(new DataLine(text,type,f2));
				} catch (IOException e) {
					errorLog.add(new LoggedWarning("Error reading Textfile "+ f1.getName()));
				}

    		} else {
    			try {
					text = FileUtils.readFileToString(f2, "UTF-8");
					resCol.add(new DataLine(text,type,f1));
				} catch (IOException e) {
					errorLog.add(new LoggedWarning("Error reading Textfile "+ f2.getName()));
				}
    		}
    	}
		return resCol;
	}

	private Boolean copy_files(Collection<DataLine> set, String dir) {
		for (DataLine dl : set) {
			String imgFileName = dl.getImageFile().getName();
			String imgFilePath = dl.getImageFile().getAbsolutePath();

			int indexfp = imgFilePath.lastIndexOf("\\");

			String txtFileName = removeExt(removeExt(imgFileName)) + ".gt.txt";
			String txtFilePath = imgFilePath.substring(0, indexfp) + "\\" + txtFileName;

			Path copiedTxtPath = Paths.get(dir + "\\" + txtFileName);
			Path copiedImgPath = Paths.get(dir + "\\" + dl.getImageFile().getName());

			// copy image
			try {
				Files.copy(dl.getImageFile().toPath(), copiedImgPath);

			} catch (IOException e) {
				errorLog.add(new LoggedWarning("Copying Img-File " + dl.getImageFile().getAbsolutePath() + " to " + copiedImgPath + " failed."));
				return false;
			}

			// copy GT text file
			try {
				Files.copy(Paths.get(txtFilePath), copiedTxtPath);
			} catch (IOException e) {
				errorLog.add(new LoggedWarning("Copying Text-File " + txtFilePath + " to " + copiedTxtPath + " failed."));
				return false;
			}
		}

		return true;
	}

	/**
	 * Does after-file-load initialization.
	 *
	 * @param fullSet Set of all DataLines loaded.
	 */
	private void afterLoadInit(Collection<DataLine> fullSet) {
		List<String> chars = new LinkedList<>();
		List<DataLineType> types = new LinkedList<>();

		for (DataLine line : fullSet) {
			for (String c : line.getText().split(""))
				if (!c.equals(" ") && !chars.contains(c))
					chars.add(c);

			if(!types.contains(line.getType()))
				types.add(line.getType());
		}

		FilterSet<DataLine> fs = Settings.getInstance().dataFilters.get("letterCounter");
		for (String c : chars)
			fs.add(new LetterCounterFilter(c));

		this.availableTypes.addAll(types);
		fs = Settings.getInstance().dataFilters.get("typeCounter");
		for (DataLineType t : types)
			fs.add(new TypeCounterFilter(t));
	}


	/**
	* Reads out the DataLineType from a Filename with the form
    * "<Pagenumber>__<...>__<DataLineType>_..." (muss nochma genauer nachschaun..)
	*
	* @param filename the filename
	* @return DataLineType of the file
	*/
	private DataLineType getType(String filename) {
		for (int i = 0; i < 2; i++) {
			int index = filename.contains("__") ? filename.indexOf("__") : filename.indexOf("_");
			filename = filename.substring(index+2, filename.length());
		}
		filename = filename.substring(0, filename.contains("__") ? filename.indexOf("__") : filename.indexOf("_"));

		return DataLineType.fromString(filename);
	}

	/**
	 * Event for clicking the "distribute"-button.
	 * Performs the SimpleDistribution.
	 */
	public void click_btn_distribute()
	{
		Collection<DataLine> items = new Collection<>();
		items.addAll(trainSet);
		items.addAll(testSet);

		SimpleDistribution dist = new SimpleDistribution();
		dist.performDistribution(items, trainSet, testSet);

		applyFilters();
		updateUI();
	}
}