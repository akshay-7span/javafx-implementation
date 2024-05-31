package calculator.utils;

import java.util.Stack;

public class MathUtils {
    public static double evaluateExpression(String expression) throws NumberFormatException, ArithmeticException, ArrayIndexOutOfBoundsException {
        String[] tokens = expression.split("(?<=[-+*/^()])|(?=[-+*/^()])");
        return evaluateTokens(tokens);
    }

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
            } else if (token.equals("(")) {
                operators.push('(');
            } else if (token.equals(")")) {
                while (operators.peek() != '(') {
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

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static boolean hasPrecedence(char op1, char op2) {
        return (op2 != '(' && op2 != ')') && (op1 != '^' || op2 != '^') && (precedence(op1) <= precedence(op2));
    }

    private static int precedence(char op) {
        if (op == '+' || op == '-') {
            return 1;
        } else if (op == '*' || op == '/') {
            return 2;
        } else if (op == '^') {
            return 3;
        } else {
            return 0;
        }
    }
}
