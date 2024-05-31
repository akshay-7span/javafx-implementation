package calculator;

import constants.Constants;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import utils.Utils;

public class ButtonFactory {

    public static GridPane createButtons(OperationHandler operationHandler) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        int row = 0;
        int col = 0;

        for (String text : Constants.BUTTONS) {
            Button button = new Button(text);
            button.setMinSize(60, 60);
            button.setFont(new Font("Source Code Pro", 18));
            button.setStyle(Utils.getButtonStyle(text));
            button.setOnAction(e -> operationHandler.handleButtonClick(text));

            if (text.equals("0")) {
                button.setMinWidth(130);
                GridPane.setColumnSpan(button, 2);
            }

            grid.add(button, col, row);

            if (text.equals("0")) {
                col += 2;
            } else {
                col++;
            }

            if (col == 4) {
                col = 0;
                row++;
            }
        }

        return grid;
    }
}
