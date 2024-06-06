package calculator.app;

import calculator.constants.CalculatorConstants;
import calculator.controller.CalculatorController;
import calculator.ui.UISetup;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculatorAppLauncher extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(CalculatorConstants.CALCULATOR);

        // Initialize the calculator controller
        CalculatorController controller = new CalculatorController();

        // Set up the user interface using the UISetup class
        UISetup uiSetup = new UISetup(controller);
        VBox root = uiSetup.setupUI();

        // Create and set the scene for the primary stage
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load previous calculation history from the database (commented out)
        // controller.getHistoryListView().getItems().addAll(DBUtils.getCalculationHistory());
    }
}
