package calculator.utils;

import calculator.constants.Constants;

public class Utils {

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getButtonStyle(String text) {
        String baseStyle = Constants.BASE_BUTTON_STYLE;
        if ("AC".equals(text) || "+/-".equals(text) || "%".equals(text)) {
            baseStyle = Constants.FUNCTION_BUTTON_STYLE;
        } else if ("÷".equals(text) || "×".equals(text) || "-".equals(text) || "+".equals(text) || "=".equals(text)) {
            baseStyle = Constants.OPERATOR_BUTTON_STYLE;
        }
        return baseStyle + Constants.BUTTON_STYLE_SUFFIX;
    }
}
