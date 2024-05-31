package calculator.ui;

import calculator.app.CalculatorApp;
import calculator.constants.CalculatorConstants;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class ButtonGridManager {
    private final GridPane gridPane;
    private final CalculatorApp calculator;

    public ButtonGridManager(GridPane gridPane, CalculatorApp calculator) {
        this.gridPane = gridPane;
        this.calculator = calculator;
    }

    public void addButtonsToGrid(String[][] buttonLabels) {
        int numRows = buttonLabels.length;
        int numCols = buttonLabels[0].length;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Button button = new Button(buttonLabels[i][j]);
                button.setPrefWidth(CalculatorConstants.BUTTON_WIDTH);
                button.setPrefHeight(CalculatorConstants.BUTTON_HEIGHT);
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> calculator.buttonClicked(buttonLabels[finalI][finalJ]));
                gridPane.add(button, j, i + 1);
            }
        }
    }

    public void clearButtonsFromGrid() {
        ObservableList<Node> children = gridPane.getChildren();
        List<Node> toRemove = new ArrayList<>();
        for (Node node : children) {
            if (node instanceof Button) {
                toRemove.add(node);
            }
        }
        children.removeAll(toRemove);
    }
}
