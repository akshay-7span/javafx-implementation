package com.javaFX.contextMenu;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ContextMenuDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Label label = new Label("Result here");
        Circle circle = new Circle();
        circle.setRadius(80);
        circle.setFill(Color.AQUA);

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(circle,label);

        Scene scene = new Scene(vBox,400,400);
        stage.setScene(scene);
        stage.show();

        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Menu item 1");
        MenuItem menuItem2 = new MenuItem("Menu item 2");
        MenuItem menuItem3 = new MenuItem("Menu item 3");

        contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3);
        circle.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent contextMenuEvent) {
                contextMenu.show(circle,contextMenuEvent.getSceneX(),contextMenuEvent.getSceneY());
            }
        });
        menuItem1.setOnAction(event -> {
            label.setText("Menu item 1 clicked");
            circle.setFill(Color.BLACK);
        });
        menuItem2.setOnAction(event -> {
            label.setText("Menu item 2 clicked");
            circle.setFill(Color.RED);
        });
        menuItem3.setOnAction(event -> {
            label.setText("Menu item 3 clicked");
            circle.setFill(Color.PURPLE);
        });
    }
}
