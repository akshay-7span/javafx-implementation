package calculator.constants;

public class Constants {

    public static String TITLE ="MAC Calculator";
    public static final String[] BUTTONS = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "="
    };

    public static final String DEFAULT_BUFFER = "0";

    public static final String BASE_BUTTON_STYLE = "-fx-background-color: #505050; -fx-text-fill: white;";
    public static final String FUNCTION_BUTTON_STYLE = "-fx-background-color: #606060; -fx-text-fill: white;";
    public static final String OPERATOR_BUTTON_STYLE = "-fx-background-color: #FF9500; -fx-text-fill: white;";
    public static final String BUTTON_STYLE_SUFFIX = " -fx-background-radius: 10px;";

    public static final String DISPLAY_STYLE = "-fx-background-color: #333333; -fx-text-fill: white; -fx-alignment: center-right;";
    public static final String ROOT_STYLE = "-fx-background-color: #333333;";

    private Constants() {
        // Prevent instantiation
    }
}
