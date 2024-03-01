package com.javaFX.splitMenuButton;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SplitMenuButtonDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        SplitMenuButton splitMenuButton = new SplitMenuButton();
        splitMenuButton.setText("Languages");
        MenuItem menuItem1 = new MenuItem("Menu item 1");
        MenuItem menuItem2 = new MenuItem("Menu item 2");
        MenuItem menuItem3 = new MenuItem("Menu item 3");

        splitMenuButton.getItems().addAll(menuItem1,menuItem2,menuItem3);

        Label label = new Label("Selected items");

        splitMenuButton.setOnAction(event -> label.setText("Button clicked"));
        menuItem1.setOnAction(event -> {
            label.setText("Menubutton1 clicked");
        });
        menuItem2.setOnAction(event -> {
            label.setText("Menubutton2 clicked");
        });
        menuItem3.setOnAction(event -> {
            label.setText("Menubutton3 clicked");
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(splitMenuButton);
        borderPane.setBottom(label);
        Scene scene = new Scene(borderPane,400,400);
        stage.setScene(scene);
        stage.show();
    }
}
