package com.javaFX.treeView;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TreeViewDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        TreeView<String> treeView = new TreeView<>();

        TreeItem<String> treeItem = new TreeItem<>("Files");
        treeView.setRoot(treeItem);

        //Creating sub nodes

        TreeItem<String> picture = new TreeItem<>("Picture");
        TreeItem<String> musics = new TreeItem<>("Musics");
        TreeItem<String> videos = new TreeItem<>("Videos");

        //adding root node
        treeItem.getChildren().addAll(picture,musics,videos);

        TreeItem<String> p1 = new TreeItem<>("Picture1");
        TreeItem<String> p2 = new TreeItem<>("Picture2");

        TreeItem<String> m1 = new TreeItem<>("Musics1");
        TreeItem<String> m2 = new TreeItem<>("Musics2");

        TreeItem<String> v1 = new TreeItem<>("Videos1");
        TreeItem<String> v2 = new TreeItem<>("Videos2");

        picture.getChildren().addAll(p1,p2);
        musics.getChildren().addAll(m1,m2);
        videos.getChildren().addAll(v1,v2);

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<String>> observable, TreeItem<String> oldValue, TreeItem<String> newValue) {
                if (newValue != null) {
                    System.out.println("Selected item: " + newValue.getValue());
                }
            }
        });
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(treeView);
        Scene scene = new Scene(borderPane,300,300);
        stage.setScene(scene);
        stage.show();
    }
}
