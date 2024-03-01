package com.javaFX.switching;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SwitchingWithScene extends Application {
    private Scene scene1,scene2;
    @Override
    public void start(Stage stage) throws Exception {
        scene1 = createScene1();
        scene2 = createScene2();

        stage.setScene(scene1);
        stage.setTitle("Switching between Scenes");
        stage.show();
    }

    private Scene createScene1() {
        VBox vBox = new VBox(100);
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("This is scene 1");
        label.setFont(Font.font(20));
        Button button = new Button("Open scene 2");
        button.setOnAction(e ->{
            setScene(scene2,e);
        });
        button.setFont(Font.font(20));
        vBox.getChildren().addAll(label,button);
        return new Scene(vBox,300,300);
    }

    private Scene createScene2() {
        VBox vBox = new VBox(100);
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("This is scene 2");
        label.setFont(Font.font(20));
        Button button = new Button("Open scene 1");
        button.setOnAction(e ->{
            setScene(scene1,e);
        });
        button.setFont(Font.font(20));
        vBox.getChildren().addAll(label,button);
        return new Scene(vBox,300,300);
    }

    private void  setScene(Scene scene, ActionEvent event){
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.setScene(scene);
    }
}
