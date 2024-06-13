package calculator.ui;

import calculator.constants.CalculatorConstants;
import calculator.controller.CalculatorController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class UISetup {
    private final CalculatorController calculatorController;

    public UISetup(CalculatorController calculatorController) {
        this.calculatorController = calculatorController;
    }

    // Sets up the user interface, including the display field and button grid
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

        ButtonGridManager buttonGridManager = new ButtonGridManager(gridPane, calculatorController);
        buttonGridManager.addButtonsToGrid(CalculatorConstants.buttonLabelsBasic);

        Button toggleButton = new Button(CalculatorConstants.SCIENTIFIC);
        toggleButton.setOnAction(e -> calculatorController.toggleMode());
        gridPane.add(toggleButton, 0, 9, 4, 1);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(displayField);

        calculatorController.setDisplayField(displayField);
        calculatorController.setGridPane(gridPane);

        vBox.getChildren().add(gridPane);

        return vBox;
    }
}
