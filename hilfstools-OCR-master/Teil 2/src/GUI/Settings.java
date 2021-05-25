package GUI;

import Classes.Data.DataLineType;
import Classes.Data.ForceTableEntry;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 *  Settings View Class.
 */
public class Settings extends Application {
    private boolean okWasPressed = false;

    /**
     * Shows the Settings View.
     *
     * @param forceTableEntries List of entries for the "Force 100% Train" table.
     * @param itemCount The number of items in the loaded data set.
	 * @param parentStage parent Stage of the Settings UI window
     *
     * @return true: "ok"-button was pressed; false: "cancel-button as clicked or Window was closed
     */
    boolean show(List<ForceTableEntry> forceTableEntries, int itemCount, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MovePreview.class.getResource("Settings.fxml"));

            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();

            stage.setTitle("Settings");
            stage.setScene(scene);

            SettingsController controller = loader.getController();
            controller.btn_ok.setOnAction(event -> {
                okWasPressed = true;
                controller.click_ok();
                ((Stage) controller.btn_ok.getScene().getWindow()).close();
            });
            controller.initData(forceTableEntries, itemCount);

            stage.initOwner(parentStage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.okWasPressed;
    }


    @Override
    public void start(Stage primaryStage) {
        // empty
        // ! DO NOT REMOVE THIS FUNCTION !
    }
}