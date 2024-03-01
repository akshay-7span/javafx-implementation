package com.javaFX.webView;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class WebViewDemo extends Application {

    TextField textField;
    WebEngine webEngine;
    Button b1,b2;
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane,600,400);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.show();

        WebView view = new WebView();
        BorderPane.setMargin(view,new Insets(10));
        borderPane.setCenter(view);

        webEngine=view.getEngine();
        webEngine.load("https://www.google.co.in/");

         textField = new TextField();
         b1=new Button("Search");
         b2=new Button("Home");
         b1.setOnAction(this::loadUrl);
         b2.setOnAction(this::loadHome);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(textField,b1,b2);
        BorderPane.setMargin(hBox,new Insets(10));
        borderPane.setTop(hBox);

    }


    private void loadUrl(ActionEvent event){
        String url = textField.getText();
        webEngine.load(url);

    }
    private void loadHome(ActionEvent event){
    webEngine.load("https://www.google.co.in/");
    }
}
