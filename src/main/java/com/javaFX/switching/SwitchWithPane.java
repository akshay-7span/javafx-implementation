package com.javaFX.switching;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SwitchWithPane extends Application {
    private BorderPane borderPane;
    private VBox pane1,pane2;
    @Override
    public void start(Stage stage) throws Exception {

        pane1=createPane1();
        pane2=createPane2();

        borderPane = new BorderPane();
        borderPane.setTop(createArea("Top area"));
        borderPane.setBottom(createArea("Left area"));
        borderPane.setLeft(createArea("Right area"));
        borderPane.setRight(createArea("Bottom area"));

        borderPane.setCenter(pane1);

        Scene scene = new Scene(borderPane,600,400);
        stage.setScene(scene);
        stage.setTitle("Switching between pane");
        stage.show();

    }

    private VBox createPane1() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("This is pane 1");
        label.setFont(Font.font(20));
        Button button = new Button("Open pane 2");
        button.setFont(Font.font(14));
        button.setOnAction(e->{
            borderPane.setCenter(pane2);
        });
        vBox.getChildren().addAll(button,label);
        return vBox;
    }

    private VBox createPane2() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label label = new Label("This is pane 2");
        label.setFont(Font.font(20));
        Button button = new Button("Open pane 1");
        button.setFont(Font.font(14));
        button.setOnAction(e->{
            borderPane.setCenter(pane1);
        });
        vBox.getChildren().addAll(button,label);
        return vBox;
    }
    private Button createArea(String str){
        Button button = new Button(str);
        button.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        return button;
    }
}
