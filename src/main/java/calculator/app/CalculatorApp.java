package calculator.app;

import calculator.constants.CalculatorConstants;
import calculator.handlers.ButtonActionHandler;
import calculator.ui.ButtonGridManager;
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

        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        displayField = new TextField();
        displayField.setEditable(false);
        displayField.setAlignment(Pos.CENTER_RIGHT);
        displayField.setPrefHeight(CalculatorConstants.DISPLAY_HEIGHT_BASIC);
        gridPane.add(displayField, 0, 0, 4, 1);

        ButtonGridManager buttonGridManager = new ButtonGridManager(gridPane, this);
        buttonGridManager.addButtonsToGrid(CalculatorConstants.buttonLabelsBasic);

        Button toggleButton = new Button(CalculatorConstants.SCIENTIFIC);
        toggleButton.setOnAction(e -> toggleMode());
        gridPane.add(toggleButton, 0, 9, 4, 1);

        historyListView = new ListView<>();
        historyListView.setPrefHeight(CalculatorConstants.HISTORY_HEIGHT_BASIC);
        VBox.setMargin(historyListView, new Insets(10, 0, 0, 0));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(displayField, historyListView);

        Scene scene = new Scene(new VBox(vBox, gridPane), 400, 500);
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

    public static void main(String[] args) {
        launch(args);
    }
}
