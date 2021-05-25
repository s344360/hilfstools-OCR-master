package GUI;

import Classes.Data.Collection;
import Classes.Data.StatTableEntry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * MovePreview View Class.
 */
public class MovePreview extends Application {
	private boolean okWasPressed = false;

	/**
	 * Shows the MovePreview View.
	 *
	 * @param statsLog Stats to show in the Stat Logs.
	 * @param referenceStatsLog The Stats Log entries of the original Stats Log.
	 *
	 * @return true: "ok"-button was pressed; false: "cancel-button as clicked or Window was closed
	 */
    boolean show(Collection<StatTableEntry> statsLog, Collection<StatTableEntry> referenceStatsLog, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MovePreview.class.getResource("MovePreview.fxml"));

			Scene scene = new Scene(loader.load());
			Stage stage = new Stage();

			stage.setTitle("Move Preview");
			stage.setScene(scene);

			MovePreviewController controller = loader.getController();
			controller.btn_ok.setOnAction(event -> {
				okWasPressed = true;
				((Stage) controller.btn_ok.getScene().getWindow()).close();
			});
			controller.initializeData(statsLog, referenceStatsLog);

			stage.initOwner(parentStage);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this.okWasPressed;
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// empty
		// ! DO NOT REMOVE FUNCTION !
	}
}