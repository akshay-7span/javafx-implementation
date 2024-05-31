package calculator;

import constants.Constants;
import exception.CalculatorException;
import utils.Utils;

public class OperationHandler {

    private double runningTotal = 0;
    private String buffer = Constants.DEFAULT_BUFFER;
    private boolean bufferLoaded = false;
    private String previousOperator = null;

    private final Display display;

    public OperationHandler(Display display) {
        this.display = display;
    }

    public void handleButtonClick(String value) {
        try {
            if (Utils.isNumeric(value) || value.equals(".")) {
                handleNumber(value);
            } else {
                handleSymbol(value);
            }
            rerender();
        } catch (CalculatorException e) {
            display.updateDisplay("Error");
        }
    }

    private void handleNumber(String value) {
        if (!bufferLoaded) {
            buffer = value;
            bufferLoaded = true;
        } else {
            buffer += value;
        }
    }

    private void handleSymbol(String value) {
        switch (value) {
            case "AC" -> {
                buffer = Constants.DEFAULT_BUFFER;
                bufferLoaded = false;
                runningTotal = 0;
                previousOperator = null;
            }
            case "=" -> {
                if (previousOperator != null) {
                    flushOperation(Double.parseDouble(buffer));
                    previousOperator = null;
                    buffer = String.valueOf(runningTotal);
                    runningTotal = 0;
                }
            }
            case "←" -> handleDelete();
            case "÷", "×", "+", "-" -> handleMath(value);
        }
    }

    private void handleDelete() {
        if (bufferLoaded && buffer.length() == 1) {
            buffer = Constants.DEFAULT_BUFFER;
            bufferLoaded = false;
        } else if (bufferLoaded) {
            buffer = buffer.substring(0, buffer.length() - 1);
        }
    }

    private void handleMath(String value) {
        if (!bufferLoaded) {
            return;
        }

        double intBuffer = Double.parseDouble(buffer);
        if (runningTotal == 0) {
            runningTotal = intBuffer;
        } else {
            flushOperation(intBuffer);
        }

        previousOperator = value;
        bufferLoaded = false;
    }

    private void flushOperation(double intBuffer) {
        try {
            switch (previousOperator) {
                case "+" -> runningTotal += intBuffer;
                case "-" -> runningTotal -= intBuffer;
                case "×" -> runningTotal *= intBuffer;
                case "÷" -> {
                    if (intBuffer != 0) {
                        runningTotal /= intBuffer;
                    } else {
                        throw new CalculatorException("Cannot divide by zero");
                    }
                }
            }
        } catch (Exception e) {
            throw new CalculatorException("Operation failed", e);
        }
    }

    private void rerender() {
        display.updateDisplay(buffer);
    }
}
