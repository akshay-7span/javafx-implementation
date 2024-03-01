module com.example.layoutmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires javafx.media;
    requires java.sql;


    opens com.javaFX to javafx.fxml;
    exports com.javaFX.helloWord;
    exports com.javaFX.layout;
    opens com.javaFX.layout to javafx.fxml;
    exports com.javaFX.checkBox;
    opens com.javaFX.checkBox to javafx.fxml;
    exports com.javaFX.textField;
    opens com.javaFX.textField to javafx.fxml;
    exports com.javaFX.radioButton;
    opens com.javaFX.radioButton to javafx.fxml;
    exports com.javaFX.eventHandler;
    exports com.javaFX.button;
    exports com.javaFX.label;


}