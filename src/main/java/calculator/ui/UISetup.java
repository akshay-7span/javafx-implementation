package calculator.ui;

import calculator.app.CalculatorApp;
import calculator.constants.CalculatorConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class UISetup {
    private final CalculatorApp calculatorApp;

    public UISetup(CalculatorApp calculatorApp) {
        this.calculatorApp = calculatorApp;
    }

    public VBox setupUI() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        TextField displayField = new TextField();
        displayField.setEditable(false);
        displayField.setAlignment(Pos.CENTER_RIGHT);
        displayField.setPrefHeight(CalculatorConstants.DISPLAY_HEIGHT_BASIC);
        gridPane.add(displayField, 0, 0, 4, 1);

        ButtonGridManager buttonGridManager = new ButtonGridManager(gridPane, calculatorApp);
        buttonGridManager.addButtonsToGrid(CalculatorConstants.buttonLabelsBasic);

        Button toggleButton = new Button(CalculatorConstants.SCIENTIFIC);
        toggleButton.setOnAction(e -> calculatorApp.toggleMode());
        gridPane.add(toggleButton, 0, 9, 4, 1);

        ListView<String> historyListView = new ListView<>();
        historyListView.setPrefHeight(CalculatorConstants.HISTORY_HEIGHT_BASIC);
        VBox.setMargin(historyListView, new Insets(10, 0, 0, 0));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(displayField, historyListView);

        calculatorApp.setDisplayField(displayField);
        calculatorApp.setHistoryListView(historyListView);
        calculatorApp.setGridPane(gridPane);

        return new VBox(vBox, gridPane);
    }
}
