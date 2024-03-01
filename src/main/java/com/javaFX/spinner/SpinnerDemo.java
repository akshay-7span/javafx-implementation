package com.javaFX.spinner;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SpinnerDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Spinner<Integer> spinner = new Spinner<>();
        SpinnerValueFactory<Integer> spinnerValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,10);

        spinner.setValueFactory(spinnerValueFactory);
        spinner.setEditable(true);
        Label label = new Label("Value is :"+spinner.getValue());
        spinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                label.setText("Value is :"+spinner.getValue());
            }
        });
        spinnerValueFactory.setValue(5);
      
        HBox hBox = new HBox();
        hBox.getChildren().addAll(spinner,label);
        
        Scene scene = new Scene(hBox,300,400);
        stage.setScene(scene);
        stage.show();
    }
}
