package calculator.controller;
import calculator.constants.CalculatorConstants;
import calculator.handlers.ButtonActionHandler;
import calculator.ui.ButtonGridManager;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
public class CalculatorController {
    private ListView<String> historyListView;
    private TextField displayField;
    private boolean isScientificMode = false;
    private GridPane gridPane;

    // Handles button click events by delegating to ButtonActionHandler
    public void buttonClicked(String buttonLabel) {
        ButtonActionHandler buttonActionHandler = new ButtonActionHandler(this, displayField, historyListView, isScientificMode, gridPane);
        buttonActionHandler.buttonClicked(buttonLabel);
    }

    // Toggles between basic and scientific mode
    public void toggleMode() {
        isScientificMode = !isScientificMode;
        String[][] buttonLabels = isScientificMode ? CalculatorConstants.buttonLabelsScientific : CalculatorConstants.buttonLabelsBasic;

        // Update button grid based on the mode
        ButtonGridManager buttonGridManager = new ButtonGridManager(gridPane, this);
        buttonGridManager.clearButtonsFromGrid();
        buttonGridManager.addButtonsToGrid(buttonLabels);

        // Adjust display and history list view sizes based on the mode
        displayField.setPrefHeight(isScientificMode ? CalculatorConstants.DISPLAY_HEIGHT_SCIENTIFIC : CalculatorConstants.DISPLAY_HEIGHT_BASIC);
        historyListView.setPrefHeight(isScientificMode ? CalculatorConstants.HISTORY_HEIGHT_SCIENTIFIC : CalculatorConstants.HISTORY_HEIGHT_BASIC);
    }

    // Setter methods for UI components
    public void setDisplayField(TextField displayField) {
        this.displayField = displayField;
    }

    public void setHistoryListView(ListView<String> historyListView) {
        this.historyListView = historyListView;
    }

    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }

}
