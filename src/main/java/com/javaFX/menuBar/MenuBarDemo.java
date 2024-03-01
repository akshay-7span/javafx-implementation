package com.javaFX.menuBar;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MenuBarDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane,400,400);
        stage.setScene(scene);
        stage.show();

        MenuBar menuBar = new MenuBar();
        borderPane.setTop(menuBar);

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu helpMenu = new Menu("Help");

        menuBar.getMenus().addAll(fileMenu,editMenu,helpMenu);

        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem cutItem = new MenuItem("Cute");
        MenuItem copyItem = new MenuItem("Copy");
        MenuItem pestItem = new MenuItem("Pest");
        MenuItem aboutItem = new MenuItem("About");

        fileMenu.getItems().addAll(newItem,openItem,saveItem,exitItem);
        editMenu.getItems().addAll(copyItem,cutItem,pestItem);
        helpMenu.getItems().add(aboutItem);

    }
}
