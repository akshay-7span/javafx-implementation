package calculator.constants;

public class CalculatorConstants {
    public static final int BUTTON_WIDTH = 50;
    public static final int BUTTON_HEIGHT = 50;
    public static final int DISPLAY_HEIGHT_BASIC = 40;
    public static final int DISPLAY_HEIGHT_SCIENTIFIC = 80;
    public static final int HISTORY_HEIGHT_BASIC = 100;
    public static final int HISTORY_HEIGHT_SCIENTIFIC = 200;

    public static final String SCIENTIFIC = "Scientific";
    public static final String CALCULATOR = "Calculator";

    // Operation symbols

    public static final String ADD = "+";
    public static final String SUBTRACT = "-";
    public static final String MULTIPLY = "*";
    public static final String DIVIDE = "/";
    public static final String CLEAR = "C";
    public static final String EQUALS = "=";
    public static final String TOGGLE = "Toggle";
    public static final String SIN = "sin";
    public static final String COS = "cos";
    public static final String TAN = "tan";
    public static final String POWER = "^";
    public static final String LOG = "log";
    public static final String SQRT = "sqrt";
    public static final String OPEN_PAREN = "(";
    public static final String CLOSE_PAREN = ")";
    public static final String ASIN = "asin";
    public static final String ACOS = "acos";
    public static final String ATAN = "atan";
    public static final String PI = "PI";
    public static final String LN = "ln";
    public static final String E = "e";
    public static final String PERCENT = "%";

    public static final String[][] buttonLabelsBasic = {
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {CLEAR, "0", EQUALS, "+"}
    };

    public static final String[][] buttonLabelsScientific = {
            {SIN, COS, TAN, POWER},
            {LOG, SQRT, OPEN_PAREN, CLOSE_PAREN},
            {ASIN, ACOS, ATAN, PI},
            {TOGGLE, LN, E, PERCENT},
            {"7", "8", "9", "/"},
            {"4", "5", "6", "*"},
            {"1", "2", "3", "-"},
            {CLEAR, "0", EQUALS, "+"}
    };
}
