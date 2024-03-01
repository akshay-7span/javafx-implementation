package com.javaFX.spinner;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StringSpinnerDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ObservableList<String> list = FXCollections.observableArrayList("Anup","Harsh","Harshil","Harshang","Ajay","Abhi");
        Spinner<String> spinner = new Spinner<>();

        SpinnerValueFactory<String> spinnerValueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(list);
        spinner.setValueFactory(spinnerValueFactory);

        Label label = new Label("Value of :"+ spinnerValueFactory.getValue());
        spinner.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                label.setText("Value of : "+spinnerValueFactory.getValue());
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(spinner,label);

        Scene scene = new Scene(hBox,300,400);
        stage.setScene(scene);
        stage.show();
    }
}
