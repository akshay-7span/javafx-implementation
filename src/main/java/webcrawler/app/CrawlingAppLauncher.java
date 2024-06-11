package webcrawler.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Objects;

import static webcrawler.ui.DataHandler.root;


public class CrawlingAppLauncher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Setting up the title of the application window
        primaryStage.setTitle("Web Crawling");

        GridPane root = root();
        // Creating the scene
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());

        primaryStage.setScene(scene);
        //primaryStage.setFullScreen(true);

        // Displaying the stage
        primaryStage.show();
    }
}
