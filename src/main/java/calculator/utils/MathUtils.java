package calculator.utils;

import calculator.constants.CalculatorConstants;

import java.util.Stack;

public class MathUtils {
    // Evaluates the given mathematical expression and returns the result as a double
    public static double evaluateExpression(String expression) throws NumberFormatException, ArithmeticException, ArrayIndexOutOfBoundsException {
        String[] tokens = expression.split("(?<=[-+*/^()])|(?=[-+*/^()])");
        return evaluateTokens(tokens);
    }

    // Evaluates an array of tokens representing a mathematical expression
    private static double evaluateTokens(String[] tokens) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (String token : tokens) {
            if (token.matches("[\\d.]+")) {
                values.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                char operator = token.charAt(0);
                while (!operators.isEmpty() && hasPrecedence(operator, operators.peek())) {
                    applyOperator(values, operators.pop());
                }
                operators.push(operator);
            } else if (token.equals(CalculatorConstants.OPEN_PAREN)) {
                operators.push(CalculatorConstants.OPEN_PAREN.charAt(0));
            } else if (token.equals(CalculatorConstants.CLOSE_PAREN)) {
                while (operators.peek() != CalculatorConstants.OPEN_PAREN.charAt(0)) {
                    applyOperator(values, operators.pop());
                }
                operators.pop();
            }
        }

        while (!operators.isEmpty()) {
            applyOperator(values, operators.pop());
        }

        return values.pop();
    }

    // Applies an operator to the top two values in the stack
    private static void applyOperator(Stack<Double> values, char operator) {
        double operand2 = values.pop();
        double operand1 = values.pop();
        switch (operator) {
            case '+' -> values.push(operand1 + operand2);
            case '-' -> values.push(operand1 - operand2);
            case '*' -> values.push(operand1 * operand2);
            case '/' -> values.push(operand1 / operand2);
            case '^' -> values.push(Math.pow(operand1, operand2));
        }
    }

    // Checks if the given character is an operator
    private static boolean isOperator(char c) {
        return c == CalculatorConstants.ADD.charAt(0) ||
                c == CalculatorConstants.SUBTRACT.charAt(0) ||
                c == CalculatorConstants.MULTIPLY.charAt(0) ||
                c == CalculatorConstants.DIVIDE.charAt(0) ||
                c == CalculatorConstants.POWER.charAt(0);
    }

    // Determines if op1 has precedence over op2
    private static boolean hasPrecedence(char op1, char op2) {
        return (op2 != CalculatorConstants.OPEN_PAREN.charAt(0) && op2 != CalculatorConstants.CLOSE_PAREN.charAt(0)) &&
                (op1 != CalculatorConstants.POWER.charAt(0) || op2 != CalculatorConstants.POWER.charAt(0)) &&
                (precedence(op1) <= precedence(op2));
    }

    // Returns the precedence of the given operator
    private static int precedence(char op) {
        if (op == CalculatorConstants.ADD.charAt(0) || op == CalculatorConstants.SUBTRACT.charAt(0)) {
            return 1;
        } else if (op == CalculatorConstants.MULTIPLY.charAt(0) || op == CalculatorConstants.DIVIDE.charAt(0)) {
            return 2;
        } else if (op == CalculatorConstants.POWER.charAt(0)) {
            return 3;
        } else {
            return 0;
        }
    }
}
