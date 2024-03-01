package com.javaFX.menuButton;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuButtonDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        MenuButton menuButton = new MenuButton("Languages");
        MenuItem menuItem1 = new MenuItem("Menu item 1");
        MenuItem menuItem2 = new MenuItem("Menu item 2");
        MenuItem menuItem3 = new MenuItem("Menu item 3");

        menuButton.getItems().addAll(menuItem1,menuItem2,menuItem3);

        Label label = new Label("Selected items");

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
        borderPane.setBottom(label);
        borderPane.setCenter(menuButton);
        Scene scene = new Scene(borderPane,400,400);
        stage.setScene(scene);
        stage.show();
    }
}
