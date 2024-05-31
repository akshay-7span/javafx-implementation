package app;

import calculator.Calculator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MacCalc extends Application {
    @Override
    public void start(Stage primaryStage) {
        Calculator calculator = new Calculator();

        VBox root = calculator.getLayout();
        Scene scene = new Scene(root, 320, 450);

        primaryStage.setTitle("JavaFX Calculator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}