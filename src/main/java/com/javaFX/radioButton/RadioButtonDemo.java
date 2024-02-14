package com.javaFX.radioButton;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class RadioButtonDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Label l1 = new Label("Select your items");
        RadioButton r1 = new RadioButton("Pizza");
        RadioButton r2 = new RadioButton("Pasta");
        RadioButton r3 = new RadioButton("Sandwich");
        r1.setFont(Font.font(10));
        r2.setFont(Font.font(10));
        r3.setFont(Font.font(10));

        Label l2 = new Label("Selected item");

        ToggleGroup toggleGroup = new ToggleGroup();
        r1.setToggleGroup(toggleGroup);
        r2.setToggleGroup(toggleGroup);
        r3.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton radioButton = (RadioButton)t1;
                l2.setText(radioButton.getText());
            }
        });

        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.getChildren().addAll(l1, r1, r2, r3, l2);

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Check box Demo");
        stage.setScene(scene);
        stage.show();
    }
}
