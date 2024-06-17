package calculator.handlers;

import calculator.constants.CalculatorConstants;
import calculator.controller.CalculatorManager;
import calculator.utils.MathUtils;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.function.Function;

public class ButtonActionHandler {
    private final TextField displayField;
    private final ListView<String> historyListView;
    private final CalculatorManager calculator;

    public ButtonActionHandler(CalculatorManager calculator, TextField displayField, ListView<String> historyListView, boolean isScientificMode, GridPane gridPane) {
        this.calculator = calculator;
        this.displayField = displayField;
        this.historyListView = historyListView;
    }

    // Handles button click events by performing the appropriate action based on the button label
    public void buttonClicked(String buttonLabel) {
        switch (buttonLabel) {
            case CalculatorConstants.CLEAR -> displayField.clear();
            case CalculatorConstants.EQUALS -> calculate();
            case CalculatorConstants.TOGGLE -> calculator.toggleMode();
            case CalculatorConstants.SIN -> performUnaryOperation(Math::sin);
            case CalculatorConstants.COS -> performUnaryOperation(Math::cos);
            case CalculatorConstants.TAN -> performUnaryOperation(Math::tan);
            case CalculatorConstants.POWER -> displayField.appendText(CalculatorConstants.POWER);
            case CalculatorConstants.LOG -> performUnaryOperation(Math::log);
            case CalculatorConstants.SQRT -> performUnaryOperation(Math::sqrt);
            case CalculatorConstants.OPEN_PAREN -> displayField.appendText(CalculatorConstants.OPEN_PAREN);
            case CalculatorConstants.CLOSE_PAREN -> displayField.appendText(CalculatorConstants.CLOSE_PAREN);
            case CalculatorConstants.ASIN -> performUnaryOperation(Math::asin);
            case CalculatorConstants.ACOS -> performUnaryOperation(Math::acos);
            case CalculatorConstants.ATAN -> performUnaryOperation(Math::atan);
            case CalculatorConstants.PI -> displayField.appendText(Double.toString(Math.PI));
            case CalculatorConstants.LN -> performUnaryOperation(Math::log);
            case CalculatorConstants.E -> displayField.appendText(Double.toString(Math.E));
            case CalculatorConstants.PERCENT -> displayField.appendText("/100");
            default -> displayField.appendText(buttonLabel);
        }
    }

    // Performs a unary operation (e.g., sin, cos) on the value in the display field
    private void performUnaryOperation(Function<Double, Double> operation) {
        try {
            double operand = Double.parseDouble(displayField.getText());
            double result = operation.apply(operand);
            displayField.setText(Double.toString(result));
        } catch (NumberFormatException e) {
            displayField.setText("Error");
        }
    }

    // Calculates the result of the expression in the display field
    private void calculate() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            try {
                double result = MathUtils.evaluateExpression(expression);
                if (!Double.isInfinite(result) && !Double.isNaN(result)) {
                    if (result == (int) result) {
                        displayField.setText(Integer.toString((int) result));
                    } else {
                        displayField.setText(Double.toString(result));
                    }
                    calculator.addHistoryEntry(expression, displayField.getText());
                } else {
                    displayField.setText("Mathematical operation cannot be performed");
                }
            } catch (Exception e) {
                displayField.setText("Error: Invalid expression or mathematical operation");
            }
        }
    }
}