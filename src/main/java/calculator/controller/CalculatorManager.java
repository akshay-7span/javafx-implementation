package calculator.controller;

import calculator.constants.CalculatorConstants;
import calculator.handlers.ButtonActionHandler;
import calculator.ui.ButtonGridManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.HashMap;

public class CalculatorManager {
    private ListView<String> historyListView; // UI component to display history entries
    private TextField displayField; // UI component to display and input calculations
    private boolean isScientificMode = false; // Flag to indicate current mode (basic or scientific)
    private GridPane gridPane; // UI component to manage button grid layout
    private final HashMap<Integer, String> historyMap = new HashMap<>(); // Map to store history entries
    private int historyIndex = 0; // Index for managing history entries
    private final ObservableList<String> historyList = FXCollections.observableArrayList(); // Observable list for history ListView

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

    // Adds an entry to the history
    public void addHistoryEntry(String expression, String result) {
        String historyEntry = expression + " = " + result;
        historyMap.put(historyIndex++, historyEntry); // Add entry to map with incremented index
        historyList.add(historyEntry); // Add entry to observable list
        historyListView.setItems(historyList); // Update ListView with new list of history entries
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
