package com.javaFX.layout;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class FlowPaneExample extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Button b1 = new Button("Java");
        Button b2 = new Button("C#");
        b2.setPrefSize(70,50);
        Button b3 = new Button("Python");
        Button b4 = new Button("Magento");
        Button b5 = new Button(".net");

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(b1,b2,b3,b4,b5);
        flowPane.setPadding(new Insets(12));
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(flowPane, 500, 400);
        stage.setTitle("Flow pane demo");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }

}
