package calculator.utils;

import calculator.constants.CalculatorConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(CalculatorConstants.DB_URL, CalculatorConstants.DB_USER, CalculatorConstants.DB_PASSWORD);
            createTableIfNotExists();
        }
        return connection;
    }

    private static void createTableIfNotExists() throws SQLException {
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

    public static void insertCalculationHistory(String calculation, double result) {
        String sql = "INSERT INTO calculation_history (calculation, result) VALUES (?, ?)";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, calculation);
            statement.setDouble(2, result);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getCalculationHistory() {
        List<String> history = new ArrayList<>();
        String sql = "SELECT calculation, result FROM calculation_history ORDER BY created_at DESC LIMIT 10";
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String calculation = resultSet.getString("calculation");
                double result = resultSet.getDouble("result");
                history.add(calculation + " = " + result);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return history;
    }
}
