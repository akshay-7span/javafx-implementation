package com.javaFX.css;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class JavaFXWithCSSExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creating Labels
        Label firstNameLabel = new Label("First Name");
        Label lastNameLabel = new Label("Last Name");
        Label emailLabel = new Label("Email");
        Label passwordLabel = new Label("Password");

        // Creating TextFields
        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();
        TextField emailTextField = new TextField();
        TextField passwordTextField = new TextField();

        // Creating Buttons
        Button submitButton = new Button("Submit");
        Button resetButton = new Button("Reset");

        // Creating Title
        Text title = new Text("JavaFX Form");
        title.setId("title");

        // Creating GridPane layout
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(title, 0, 0, 2, 1);
        gridPane.add(firstNameLabel, 0, 1);
        gridPane.add(firstNameTextField, 1, 1);
        gridPane.add(lastNameLabel, 0, 2);
        gridPane.add(lastNameTextField, 1, 2);
        gridPane.add(emailLabel, 0, 3);
        gridPane.add(emailTextField, 1, 3);
        gridPane.add(passwordLabel, 0, 4);
        gridPane.add(passwordTextField, 1, 4);
        gridPane.add(submitButton, 0, 5);
        gridPane.add(resetButton, 1, 5);

        // Adding CSS file to the scene
        Scene scene = new Scene(gridPane, 500, 400);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("JavaFX with CSS Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
