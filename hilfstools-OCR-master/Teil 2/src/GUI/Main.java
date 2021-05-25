package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 *  Main View Class.
 */
public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
    	this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Distribution Tool");
        initRootLayout();
    }
    
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Main.fxml"));

            BorderPane rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);

            MainController controller = loader.getController();
			controller.btn_move_to_train.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/GUI/img/arrow_left.png"), 25, 14, false, false)));
			controller.btn_move_to_test.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/GUI/img/arrow_right.png"), 25, 14, false, false)));


            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
