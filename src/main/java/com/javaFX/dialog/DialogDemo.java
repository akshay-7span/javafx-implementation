package com.javaFX.dialog;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class DialogDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        VBox vBox = new VBox();

        Button b1 =new Button("Simple");
        Button b2 =new Button("Info");
        Button b3 =new Button("Warning");
        Button b4 =new Button("Error");
        Button b5 =new Button("Confirmation");

        vBox.getChildren().addAll(b1,b2,b3,b4,b5);

        b1.setOnAction(e->showSimpleAlert());
        b2.setOnAction(e->showInformationAlert());


        Scene scene = new Scene(vBox,400,400);
        stage.setScene(scene);
        stage.show();
//        showSimpleAlert();
//        showInformationAlert();
    }

    private void showInformationAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Text connection");
        alert.setHeaderText("Result");
        alert.setContentText("Connected to the database successfully");
        alert.show();
    }
    private void showSimpleAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Simple alert");
        alert.setHeaderText("demo");
        alert.setContentText("This is my message");
        Optional<ButtonType> result =  alert.showAndWait();
        if (result.get()==ButtonType.OK){
            System.out.println("File deleted successfully");
        }else if (result.get()==ButtonType.CANCEL){
            System.out.println("Cancelled");
        }
        alert.show();
    }
}
