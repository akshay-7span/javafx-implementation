package com.javaFX.layout;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Button b1 = new Button("Java");
        Button b2 = new Button("C#");
        Button b3 = new Button("Python");
        Button b4 = new Button("Magento");
        Button b5 = new Button(".net");


        GridPane gridPane = new GridPane();

        gridPane.add(b1,0,0,1,1);
        gridPane.add(b2,1,0,1,1);
        gridPane.add(b3,2,0,1,1);
        gridPane.add(b4,1,1,1,1);
        gridPane.add(b5,2,1,1,1);
        gridPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(gridPane, 500, 400);
        stage.setTitle("GridPane demo");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
