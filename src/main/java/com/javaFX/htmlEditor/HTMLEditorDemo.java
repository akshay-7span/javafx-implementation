package com.javaFX.htmlEditor;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

public class HTMLEditorDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setHtmlText("This is my HTML demo");

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(htmlEditor);

        Scene scene = new Scene(borderPane,400,400);
        stage.setScene(scene);
        stage.setTitle("HTML editor demo");
        stage.show();
    }
}
