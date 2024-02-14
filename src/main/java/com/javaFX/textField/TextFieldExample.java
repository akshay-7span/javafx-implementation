package com.javaFX.textField;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TextFieldExample extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        HBox hBox = new HBox();
        TextField textField = new TextField();
        textField.setPromptText("Enter your name");
        textField.setFocusTraversable(false);
        textField.setOnAction(e-> {
            System.out.println(textField.getText());
            textField.setText("");
        });

        hBox.getChildren().add(textField);
        Scene scene = new Scene(hBox, 500, 400);
        stage.setTitle("Text field demo");
        stage.setScene(scene);
        stage.show();
    }
}
