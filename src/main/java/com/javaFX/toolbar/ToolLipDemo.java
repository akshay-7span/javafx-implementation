package com.javaFX.toolbar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ToolLipDemo extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Tooltip tooltip = new Tooltip();
        Button button = new Button("New");
        tooltip.setText("Create new button");
        Font font = new Font(20);
        button.setTooltip(tooltip);
        tooltip.setFont(font);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(button);
        Scene scene = new Scene(borderPane,300,300);
        stage.setScene(scene);
        stage.show();
    }
}
