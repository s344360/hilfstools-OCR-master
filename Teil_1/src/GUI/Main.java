package GUI;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Main View Class.
 */

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	static ScrollPane scroll = new ScrollPane();

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		this.primaryStage.setTitle("Region Selection Tool");

		initRootLayout();
	}

	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();

			loader.setLocation(Main.class.getResource("Main.fxml"));

			rootLayout = (BorderPane) loader.load();
			Main_Controller controller = (Main_Controller) loader.getController();
			controller.init(primaryStage);
			controller.init2(rootLayout);

			Scene scene = new Scene(rootLayout);

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
