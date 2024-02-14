package com.javaFX.layout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class TilePaneExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Button b1 = new Button("Java");
        Button b2 = new Button("C#");
        b2.setPrefSize(70,50);
        Button b3 = new Button("Python");
        Button b4 = new Button("Magento");
        Button b5 = new Button(".net");

        TilePane tilePane = new TilePane();
        tilePane.getChildren().addAll(b1,b2,b3,b4,b5);
        tilePane.setPadding(new Insets(12));
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setAlignment(Pos.CENTER);


        Scene scene = new Scene(tilePane, 500, 400);
        stage.setTitle("Flow pane demo");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}
