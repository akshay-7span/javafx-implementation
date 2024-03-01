package com.javaFX.scrollPane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ScrollPaneDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane,300,400);

        ScrollPane scrollPane = new ScrollPane();
        //FileInputStream file = new FileInputStream("nikhil-mishra-sshi-ram-ji-4k-1-1a-wm.jpg");
        Image image = new Image("nikhil-mishra-sshi-ram-ji-4k-1-1a-wm.jpg");
        ImageView imageView = new ImageView(image);

        scrollPane.setContent(imageView);
        borderPane.setCenter(scrollPane);
        stage.setScene(scene);
        stage.show();
    }
}
