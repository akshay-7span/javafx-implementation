package calculator.ui;

import calculator.constants.Constants;
import calculator.handlers.OperationHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class Calculator {

    private final Display display;
    private final OperationHandler operationHandler;

    public Calculator() {
        display = new Display();
        operationHandler = new OperationHandler(display);
    }

    public VBox getLayout() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle(Constants.ROOT_STYLE);

        GridPane buttons = ButtonFactory.createButtons(operationHandler);

        vbox.getChildren().addAll(display.getDisplayLabel(), buttons);

        return vbox;
    }
}
