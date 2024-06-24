package calculator.app;

import calculator.ui.Calculator;
import calculator.constants.Constants;
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

            primaryStage.setTitle(Constants.TITLE);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }