module com.example.calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;
    requires java.sql;


    opens app to javafx.fxml;
    exports app;
}