package calculator;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class Calculator extends Application {
    private ListView<String> historyListView; // New ListView for history

    private TextField displayField;
    private boolean isScientificMode = false;
    private GridPane gridPane;

    private Button[][] buttons;
    private String[][] buttonLabelsBasic = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"C", "0", "=", "+"}
    };

    private String[][] buttonLabelsScientific = {
            {"sin", "cos", "tan", "^"},
            {"log", "sqrt", "(", ")"},
            {"asin", "acos", "atan", "PI"},
            {"Toggle", "ln", "e", "%"},
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"C", "0", "=", "+"}
    };

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Simple Calculator");

        // Initialize the grid pane
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Create the display field
        displayField = new TextField();
        displayField.setEditable(false);
        displayField.setAlignment(Pos.CENTER_RIGHT);
        displayField.setPrefHeight(40); // Increase TextField size
        gridPane.add(displayField, 0, 0, 4, 1);

        buttons = new Button[8][4];

        // Create and add buttons to the grid pane
        String[][] buttonLabels = buttonLabelsBasic;
        for (int i = 0; i < buttonLabels.length; i++) {
            for (int j = 0; j < buttonLabels[i].length; j++) {
                buttons[i][j] = new Button(buttonLabels[i][j]);
                buttons[i][j].setPrefWidth(40);
                buttons[i][j].setPrefHeight(40);
                int finalI = i;
                int finalJ = j;
                buttons[i][j].setOnAction(e -> buttonClicked(buttonLabels[finalI][finalJ]));
                gridPane.add(buttons[i][j], j, i + 1);
            }
        }

        // Add the mode toggle button for scientific mode
        Button toggleButton = new Button("Scientific");
        toggleButton.setOnAction(e -> toggleMode());
        gridPane.add(toggleButton, 0, 9, 4, 1); // Positioned at row 9, spanning 4 columns

        historyListView = new ListView<>();
        historyListView.setPrefHeight(100);
        VBox.setMargin(historyListView, new Insets(10, 0, 0, 0));

        // Create a VBox to hold the displayField and the operation history
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(displayField, historyListView);

        // Create scene and set it on the stage
        Scene scene = new Scene(new VBox(vBox, gridPane), 400, 500); // Increased height to accommodate larger TextField
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            // Connect to the database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTableIfNotExists(); // Create the table if it doesn't exist
            displayCalculationHistory();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void toggleMode() {
        isScientificMode = !isScientificMode;
        String[][] buttonLabels = isScientificMode ? buttonLabelsScientific : buttonLabelsBasic;
        int numRows = buttonLabels.length;
        int numCols = buttonLabels[0].length;

        // Clear only the buttons from the grid pane
        clearButtonsFromGrid(numRows);

        // Add buttons to the grid pane based on the selected mode
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Button button = new Button(buttonLabels[i][j]);
                button.setPrefWidth(50); // Adjust button width as needed
                button.setPrefHeight(50); // Adjust button height as needed
                int finalI = i;
                int finalJ = j;
                button.setOnAction(e -> buttonClicked(buttonLabels[finalI][finalJ]));
                gridPane.add(button, j, i + 2); // Start from row 2 for buttons
            }
        }

        // Adjust sizes of displayField and historyListView for Scientific mode
        if (isScientificMode) {
            displayField.setPadding(new Insets(10));
            displayField.setPrefHeight(80); // Increase displayField height
            historyListView.setPrefHeight(200); // Increase historyListView height
        } else {
            displayField.setPrefHeight(40); // Reset displayField height for Basic mode
            historyListView.setPrefHeight(100); // Reset historyListView height for Basic mode
        }
    }


    private void clearButtonsFromGrid(int numRows) {
        ObservableList<Node> children = gridPane.getChildren();
        List<Node> toRemove = new ArrayList<>();
        // Find and collect buttons in the grid pane
        for (Node node : children) {
            if (node instanceof Button) {
                toRemove.add(node);
            }
        }
        // Remove the buttons from the grid pane
        children.removeAll(toRemove);
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS calculation_history ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "calculation VARCHAR(255) NOT NULL,"
                + "result DOUBLE NOT NULL,"
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    private void buttonClicked(String buttonLabel) {
        switch (buttonLabel) {
            case "C" -> displayField.clear();
            case "=" -> calculate();
            case "Toggle" -> toggleMode();
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
                double result = evaluateExpression(expression);
                if (!Double.isInfinite(result) && !Double.isNaN(result)) {
                    displayField.setText(Double.toString(result));

                    // Add the new calculation to the history viewer
                    String historyEntry = expression + " = " + result;
                    historyListView.getItems().add(historyEntry);

                    // Insert calculation history into the database
                    insertCalculationHistory(expression, result);
                } else {
                    displayField.setText("Mathematical operation cannot be performed");
                }
            } catch (Exception e) {
                displayField.setText("Error: Invalid expression or mathematical operation");
            }
        }
    }


    private double evaluateExpression(String expression) {
        // Split the expression into tokens based on operators and functions
        String[] tokens = expression.split("(?<=[-+*/^()])|(?=[-+*/^()])");

        // Evaluate the expression based on the tokens
        try {
            return evaluateTokens(tokens);
        } catch (NumberFormatException | ArithmeticException | ArrayIndexOutOfBoundsException e) {
            // Handle any exceptions that may occur during evaluation
            return Double.NaN; // Indicate an error in evaluation
        }
    }

    private double evaluateTokens(String[] tokens) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (String token : tokens) {
            if (token.matches("[\\d.]+")) {
                // If the token is a number, push it onto the values stack
                values.push(Double.parseDouble(token));
            } else if (isOperator(token.charAt(0))) {
                // If the token is an operator, apply it to the top two values on the stack
                char operator = token.charAt(0);
                while (!operators.isEmpty() && hasPrecedence(operator, operators.peek())) {
                    applyOperator(values, operators.pop());
                }
                operators.push(operator);
            } else if (token.equals("(")) {
                // If the token is an opening parenthesis, push it onto the operators stack
                operators.push('(');
            } else if (token.equals(")")) {
                // If the token is a closing parenthesis, apply operators until reaching the opening parenthesis
                while (operators.peek() != '(') {
                    applyOperator(values, operators.pop());
                }
                operators.pop(); // Discard the opening parenthesis
            }
        }

        // Apply remaining operators
        while (!operators.isEmpty()) {
            applyOperator(values, operators.pop());
        }

        // The final result is the value remaining on the stack
        return values.pop();
    }

    private void applyOperator(Stack<Double> values, char operator) {
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

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private boolean hasPrecedence(char op1, char op2) {
        return (op2 != '(' && op2 != ')') && (op1 != '^' || op2 != '^') && (precedence(op1) <= precedence(op2));
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') {
            return 1;
        } else if (op == '*' || op == '/') {
            return 2;
        } else if (op == '^') {
            return 3;
        } else {
            return 0; // Parentheses have the lowest precedence
        }
    }


    private void insertCalculationHistory(String calculation, double result) {
        String sql = "INSERT INTO calculation_history (calculation, result) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, calculation);
            statement.setDouble(2, result);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCalculationHistory() {
        try {
            // Fetch calculation history from the database
            String sql = "SELECT calculation, result FROM calculation_history ORDER BY created_at DESC LIMIT 10";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            // Populate the ListView with calculation history
            while (resultSet.next()) {
                String calculation = resultSet.getString("calculation");
                double result = resultSet.getDouble("result");
                historyListView.getItems().add(calculation + " = " + result);
            }

            // Close resources
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

