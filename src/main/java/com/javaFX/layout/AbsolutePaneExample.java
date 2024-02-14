package com.javaFX.layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AbsolutePaneExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Button b1 = new Button("First button");
        Button b2 = new Button("Second button");
        Button b3 = new Button("Third button");

        b3.setLayoutX(50);
        b3.setLayoutY(50);

        Pane pane = new Pane();
        pane.getChildren().add(b1);
        pane.getChildren().add(b2);
        pane.getChildren().add(b3);

        b1.relocate(100,100);

        Scene scene = new Scene(pane, 500, 400);
        stage.setTitle("Absolut pane demo");
        stage.setScene(scene);
        stage.show();
    }
}
