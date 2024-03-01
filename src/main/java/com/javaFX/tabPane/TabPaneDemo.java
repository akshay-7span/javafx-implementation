package com.javaFX.tabPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TabPaneDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        TabPane tabPane = new TabPane();

        for (int i =0;i<=5;i++){
            Tab tab = new Tab();
            tab.setText("Tab"+i);
            StackPane pane = new StackPane();
            pane.getChildren().add(new Label("This is page :"+i));
            tab.setContent(pane);
            tabPane.getTabs().add(tab);
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(tabPane);
        Scene scene = new Scene(borderPane,300,300);
        stage.setScene(scene);
        stage.show();
    }
}
