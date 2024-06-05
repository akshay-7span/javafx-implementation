package calculator.app;

import calculator.constants.CalculatorConstants;
import calculator.handlers.ButtonActionHandler;
import calculator.ui.ButtonGridManager;
import calculator.ui.UISetup;
import calculator.utils.DBUtils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CalculatorApp extends Application {
    private ListView<String> historyListView;
    private TextField displayField;
    private boolean isScientificMode = false;
    private GridPane gridPane;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(CalculatorConstants.CALCULATOR);

        UISetup uiSetup = new UISetup(this);
        VBox root = uiSetup.setupUI();

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        historyListView.getItems().addAll(DBUtils.getCalculationHistory());
    }

    public void buttonClicked(String buttonLabel) {
        ButtonActionHandler buttonActionHandler = new ButtonActionHandler(this, displayField, historyListView, isScientificMode, gridPane);
        buttonActionHandler.buttonClicked(buttonLabel);
    }

    public void toggleMode() {
        isScientificMode = !isScientificMode;
        String[][] buttonLabels = isScientificMode ? CalculatorConstants.buttonLabelsScientific : CalculatorConstants.buttonLabelsBasic;

        ButtonGridManager buttonGridManager = new ButtonGridManager(gridPane, this);
        buttonGridManager.clearButtonsFromGrid();
        buttonGridManager.addButtonsToGrid(buttonLabels);

        displayField.setPrefHeight(isScientificMode ? CalculatorConstants.DISPLAY_HEIGHT_SCIENTIFIC : CalculatorConstants.DISPLAY_HEIGHT_BASIC);
        historyListView.setPrefHeight(isScientificMode ? CalculatorConstants.HISTORY_HEIGHT_SCIENTIFIC : CalculatorConstants.HISTORY_HEIGHT_BASIC);
    }

    public void setDisplayField(TextField displayField) {
        this.displayField = displayField;
    }

    public void setHistoryListView(ListView<String> historyListView) {
        this.historyListView = historyListView;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
