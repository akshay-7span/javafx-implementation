package com.javaFX.checkBox;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CheckBoxDemo extends Application implements EventHandler<ActionEvent> {
private CheckBox c1,c2,c3;
private Label l1,l2;
    @Override
    public void start(Stage stage) throws Exception {
         l1 = new Label("Select your items");
         c1 = new CheckBox("Pizza");
         c2 = new CheckBox("Pasta");
         c3 = new CheckBox("Sandwich");
        c1.setFont(Font.font(10));
        c2.setFont(Font.font(10));
        c3.setFont(Font.font(10));

         l2 = new Label("Selected item");

         c1.setOnAction(this);
         c2.setOnAction(this);
         c3.setOnAction(this);



     /*   c1.setOnAction(actionEvent -> {
            if (c1.isSelected()){
                System.out.println("Item selected");
            }
            else
                System.out.println("Item not selected");
        });

        c1.setIndeterminate(true);*/



        VBox root = new VBox();
        root.setPadding(new Insets(10));
        root.getChildren().addAll(l1,c1,c2,c3,l2);

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Check box Demo");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handle(ActionEvent actionEvent) {
    String srt = "Your selected item:\n";
    if (c1.isSelected()){
        srt +=c1.getText()+"\n";
    }
        if (c2.isSelected()){
            srt +=c2.getText()+"\n";
        }
        if (c3.isSelected()){
            srt +=c3.getText()+"\n";
        }

        l2.setText(srt);
    }
}
