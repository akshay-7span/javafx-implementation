package com.javaFX.toolbar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ToolBarDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ToolBar toolBar = new ToolBar();
        Button b1 =new Button("A");
        Button b2 =new Button("B");
        Button b3 =new Button("C");
        Button b4 =new Button("D");
        toolBar.getItems().addAll(b1,b2,b3,b4);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(toolBar);
        Scene scene = new Scene(borderPane,300,300);
        stage.setScene(scene);
        stage.show();
    }
}
