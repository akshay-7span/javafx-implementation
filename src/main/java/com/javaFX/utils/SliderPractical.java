package com.javaFX.utils;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SliderPractical extends Application {
    public void start(Stage stage) throws Exception {

        Rectangle rectangle = new Rectangle(100,100);
        Text text = new Text("100");
        text.setFont(Font.font((double) 100 /5));
        text.setFill(Color.WHITE);

        Slider slider = new Slider(50,400,100);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);

        StackPane root1 = new StackPane();
        root1.getChildren().add(rectangle);
        root1.getChildren().add(text);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root1);
        borderPane.setBottom(slider);
        BorderPane.setMargin(slider,new Insets(20));

        Scene scene = new Scene(borderPane,500,600);
        stage.setScene(scene);
        stage.setTitle("Slider practical");
        stage.show();

        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                rectangle.setWidth(t1.doubleValue());
                rectangle.setHeight(t1.doubleValue());

                text.setText(String.valueOf(t1.longValue()));
                text.setFont(Font.font(t1.doubleValue()/5));
            }
        });
    }
}
