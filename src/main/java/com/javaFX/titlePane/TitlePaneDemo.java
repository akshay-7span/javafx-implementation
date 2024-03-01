package com.javaFX.titlePane;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TitlePaneDemo extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        TitledPane titledPane = new TitledPane();
        titledPane.setText("My title pane");
        titledPane.setContent(new Label("This is my title pane demo \n line1 \nline2 \nline3 "));

        //change by default expanded
        titledPane.setExpanded(false);

        //un-collapsible
        titledPane.setCollapsible(true);

        //remove animation
        titledPane.setAnimated(false);

        VBox vBox = new VBox();
        vBox.getChildren().add(titledPane);

        Scene scene = new Scene(vBox,300,300);
        stage.setScene(scene);
        stage.show();
    }
}
