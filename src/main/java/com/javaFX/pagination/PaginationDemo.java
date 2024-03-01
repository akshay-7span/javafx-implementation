package com.javaFX.pagination;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PaginationDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pagination pagination = new Pagination();
        pagination.setPageCount(20);
        pagination.setPageFactory(this::createPage);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pagination);

        Scene scene = new Scene(borderPane,400,400);
        stage.setScene(scene);
        stage.show();
    }

    private Parent createPage(Integer pageIndex) {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        Label label1=new Label("Content of page index : "+(pageIndex+1));
        Label label2=new Label("This is Content for page index : "+(pageIndex+1));
        label1.setFont(Font.font(10));
        vBox.getChildren().addAll(label1,label2);
        return vBox;
    }
}
