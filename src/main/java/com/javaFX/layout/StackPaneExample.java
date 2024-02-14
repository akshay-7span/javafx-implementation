package com.javaFX.layout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class StackPaneExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Rectangle r1 = new Rectangle(250,250, Color.RED);
        Rectangle r2 = new Rectangle(200,200, Color.BLUE);
        Rectangle r3 = new Rectangle(150,150, Color.GREEN);
        Rectangle r4 = new Rectangle(100,100, Color.YELLOW);
        Rectangle r5 = new Rectangle(50,50, Color.WHITE);

        r1.setStroke(Color.BLACK);
        r2.setStroke(Color.BLACK);
        r3.setStroke(Color.BLACK);
        r4.setStroke(Color.BLACK);
        r5.setStroke(Color.BLACK);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(r1);
        stackPane.getChildren().add(r2);
        stackPane.getChildren().add(r3);
        stackPane.getChildren().add(r4);
        stackPane.getChildren().add(r5);


        Scene scene = new Scene(stackPane, 500, 400);
        stage.setTitle("Stack pane demo");
        stage.setScene(scene);
        stage.show();
    }
}
