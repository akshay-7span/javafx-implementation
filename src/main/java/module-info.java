module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.bytedeco.javacv;
    requires org.bytedeco.ffmpeg;


    opens com.javaFX.recorder to javafx.fxml;
    exports com.javaFX.recorder;
}