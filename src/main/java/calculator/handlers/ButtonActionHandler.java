package calculator.handlers;

import calculator.app.CalculatorApp;
import calculator.constants.CalculatorConstants;
import calculator.ui.ButtonGridManager;
import calculator.utils.DBUtils;
import calculator.utils.MathUtils;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.function.Function;

public class ButtonActionHandler {
    private final TextField displayField;
    private final ListView<String> historyListView;
    private final CalculatorApp calculator;

    public ButtonActionHandler(CalculatorApp calculator, TextField displayField, ListView<String> historyListView, boolean isScientificMode, GridPane gridPane) {
        this.calculator = calculator;
        this.displayField = displayField;
        this.historyListView = historyListView;
    }

    public void buttonClicked(String buttonLabel) {
        switch (buttonLabel) {
            case "C" -> displayField.clear();
            case "=" -> calculate();
            case "Toggle" -> calculator.toggleMode();
            case "sin" -> performUnaryOperation(Math::sin);
            case "cos" -> performUnaryOperation(Math::cos);
            case "tan" -> performUnaryOperation(Math::tan);
            case "^" -> displayField.appendText("^");
            case "log" -> performUnaryOperation(Math::log);
            case "sqrt" -> performUnaryOperation(Math::sqrt);
            case "(" -> displayField.appendText("(");
            case ")" -> displayField.appendText(")");
            case "asin" -> performUnaryOperation(Math::asin);
            case "acos" -> performUnaryOperation(Math::acos);
            case "atan" -> performUnaryOperation(Math::atan);
            case "PI" -> displayField.appendText(Double.toString(Math.PI));
            case "ln" -> performUnaryOperation(Math::log);
            case "e" -> displayField.appendText(Double.toString(Math.E));
            case "%" -> displayField.appendText("/100");
            default -> displayField.appendText(buttonLabel);
        }
    }

    private void performUnaryOperation(Function<Double, Double> operation) {
        try {
            double operand = Double.parseDouble(displayField.getText());
            double result = operation.apply(operand);
            displayField.setText(Double.toString(result));
        } catch (NumberFormatException e) {
            displayField.setText("Error");
        }
    }


    private void calculate() {
        String expression = displayField.getText();
        if (!expression.isEmpty()) {
            try {
                double result = MathUtils.evaluateExpression(expression);
                if (!Double.isInfinite(result) && !Double.isNaN(result)) {
                    displayField.setText(Double.toString(result));
                    String historyEntry = expression + " = " + result;
                    historyListView.getItems().add(historyEntry);
                    DBUtils.insertCalculationHistory(expression, result);
                } else {
                    displayField.setText("Mathematical operation cannot be performed");
                }
            } catch (Exception e) {
                displayField.setText("Error: Invalid expression or mathematical operation");
            }
        }
    }
}
