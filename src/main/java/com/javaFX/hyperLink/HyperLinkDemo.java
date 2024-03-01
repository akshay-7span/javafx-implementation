package com.javaFX.hyperLink;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;


public class HyperLinkDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Hyperlink hyperlink = new Hyperlink("Click here");
        Font font = new Font(10);
        hyperlink.setFont(font);
        hyperlink.setOnAction(e->{
           openWebPage("https://www.google.co.in/");
        });
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hyperlink);

        Scene scene = new Scene(vBox,400,400);
        stage.setScene(scene);
        stage.show();
    }

    private static void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
