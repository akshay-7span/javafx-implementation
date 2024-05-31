package calculator.constants;

public class CalculatorConstants {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/demo";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "root";

    public static final int BUTTON_WIDTH = 50;
    public static final int BUTTON_HEIGHT = 50;
    public static final int DISPLAY_HEIGHT_BASIC = 40;
    public static final int DISPLAY_HEIGHT_SCIENTIFIC = 80;
    public static final int HISTORY_HEIGHT_BASIC = 100;
    public static final int HISTORY_HEIGHT_SCIENTIFIC = 200;

    public static final String SCIENTIFIC = "Scientific";
    public static final String CALCULATOR = "Calculator";

    public static final String[][] buttonLabelsBasic = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"C", "0", "=", "+"}
    };

    public static final String[][] buttonLabelsScientific = {
            {"sin", "cos", "tan", "^"},
            {"log", "sqrt", "(", ")"},
            {"asin", "acos", "atan", "PI"},
            {"Toggle", "ln", "e", "%"},
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {"C", "0", "=", "+"}
    };
}
