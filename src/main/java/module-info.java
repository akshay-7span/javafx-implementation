module com.example.layoutmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires javafx.media;
    requires java.sql;


    exports com.javaFX.utils;
    exports com.javaFX.switching;
    exports com.javaFX.scrollPane;
    exports com.javaFX.spinner;
    exports com.javaFX.toolbar;
    exports com.javaFX.treeView;
    exports com.javaFX.titlePane;
    exports com.javaFX.tabPane;
    exports com.javaFX.htmlEditor;
    exports com.javaFX.hyperLink;
    exports com.javaFX.pagination;
    exports com.javaFX.mediaView;
    exports com.javaFX.menuBar;
    exports com.javaFX.contextMenu;
    exports com.javaFX.menuButton;
    exports com.javaFX.splitMenuButton;
    exports com.javaFX.dialog;
    exports com.javaFX.webView;
    exports com.javaFX.databaseCall;
    exports com.javaFX.css;
    exports com.javaFX.layout;
    opens com.javaFX.toolbar to javafx.fxml;
}