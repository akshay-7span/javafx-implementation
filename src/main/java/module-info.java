module com.example.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;
    requires java.sql;


    opens calculator.app to javafx.fxml;
    exports calculator.app;

}