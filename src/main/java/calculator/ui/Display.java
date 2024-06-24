package calculator.ui;

import calculator.constants.Constants;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Display {
    private final Label displayLabel;

    public Display() {
        displayLabel = new Label(Constants.DEFAULT_BUFFER);
        displayLabel.setFont(new Font("Source Code Pro", 36));
        displayLabel.setMinWidth(280);
        displayLabel.setMinHeight(70);
        displayLabel.setStyle(Constants.DISPLAY_STYLE);
    }

    public Label getDisplayLabel() {
        return displayLabel;
    }

    public void updateDisplay(String text) {
        displayLabel.setText(text);
    }

    public void reset() {
        displayLabel.setText(Constants.DEFAULT_BUFFER);
    }
}
