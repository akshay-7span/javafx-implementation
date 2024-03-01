package com.javaFX.mediaView;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class MediaViewDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        File file = new File("C:\\Users\\Anup\\Downloads\\ram siya ram.mp4");
        String url = file.toURI().toURL().toString();
        Media media = new Media(url);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        //mediaPlayer.setAutoPlay(true);
        MediaView mediaView = new MediaView(mediaPlayer);

        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(mediaView);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        Button playeButton = new Button("Play");
        Button pushButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button resetButton = new Button("Reset");

        playeButton.setOnAction(e->mediaPlayer.play());
        pushButton.setOnAction(e->mediaPlayer.pause());
        stopButton.setOnAction(e->mediaPlayer.stop());
        resetButton.setOnAction(e->mediaPlayer.seek(Duration.ZERO));
        hBox.getChildren().addAll(playeButton,pushButton,stopButton,resetButton);
        hBox.setPadding(new Insets(10));
        box.getChildren().add(hBox);

        Scene scene = new Scene(box,720,720);
        stage.setScene(scene);
        stage.show();

    }
}
