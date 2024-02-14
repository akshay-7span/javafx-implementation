package com.javaFX.layout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VboxExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Button b1 = new Button("Java");
        Button b2 = new Button("C#");
        b2.setPrefSize(70,50);
        Button b3 = new Button("Python");
        Button b4 = new Button("Magento");
        Button b5 = new Button(".net");
        b1.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        b2.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        b3.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        b4.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        b5.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);


        VBox vBox = new VBox();
        vBox.getChildren().addAll(b1,b2,b3,b4,b5);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(12));
        vBox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(vBox, 500, 400);
        stage.setTitle("VBox demo");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
