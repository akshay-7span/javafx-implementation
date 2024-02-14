package com.javaFX.layout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BorderPane extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        javafx.scene.layout.BorderPane borderPane = new javafx.scene.layout.BorderPane();

        Button top = createButton("Top");
        Button left = createButton("left");
        Button right = createButton("right");
        Button bottom = createButton("bottom");
        Button center = createButton("center");

        borderPane.setTop(top);
        borderPane.setRight(right);
        borderPane.setLeft(left);
        borderPane.setCenter(center);
        borderPane.setBottom(bottom);
        borderPane.setPadding(new Insets(10));

        Scene scene = new Scene(borderPane, 500, 400);
        stage.setTitle("Layout demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private Button createButton(String str){
        Button button = new Button(str);
        button.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        return button;
    }
}