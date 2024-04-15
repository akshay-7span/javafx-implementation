module crawler{
    requires javafx.controls;
    requires javafx.fxml;
    requires htmlunit;
    requires java.logging;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires jsoup;


    exports webcrawler;
    exports webcrawler.newUI;
}