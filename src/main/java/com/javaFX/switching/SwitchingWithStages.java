package com.javaFX.switching;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SwitchingWithStages extends Application {
    private Stage stage1,stage2;
    @Override
    public void start(Stage stage) throws Exception {

        stage1 = createStage1();
        stage2 = createStage2();

        stage1.show();  

    }
    private Stage createStage1() {
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Stage 1");
        VBox vBox = new VBox(10);
        Button button = new Button("Open stage 2");
        button.setOnAction(e->{
            stage2.show();
        });
        vBox.getChildren().add(button);
        stage.setScene(new Scene(vBox,400,400));

        return stage;

    }
    private Stage createStage2() {
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Stage 2");

        stage.initModality(Modality.NONE);
        stage.initOwner(stage2);

        VBox vBox = new VBox(10);
        Button button = new Button("Close stage 2");
        button.setOnAction(e->{
            stage2.close();
        });
        vBox.getChildren().add(button);
        stage.setScene(new Scene(vBox,400,500));

        return stage;
    }


}
