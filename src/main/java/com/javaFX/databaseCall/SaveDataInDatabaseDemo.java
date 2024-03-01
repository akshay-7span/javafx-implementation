package com.javaFX.databaseCall;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SaveDataInDatabaseDemo extends Application {
    private Connection connection;
    private TextField nameField;
    private TextField ageField;
    private ListView<String> listView;
    private final ObservableList<String> data = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        try {
            // Connect to MySQL database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root");

            nameField = new TextField();
            nameField.setPromptText("Enter name");
            ageField = new TextField();
            ageField.setPromptText("Enter age");

            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> saveData());

            Button updateButton = new Button("Update");
            updateButton.setOnAction(event -> updateData());

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deleteData());

            HBox buttons = new HBox(10, saveButton, updateButton, deleteButton);

            listView = new ListView<>();
            retrieveData();

            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    String[] parts = newValue.split(" - ");
                    nameField.setText(parts[0]);
                    ageField.setText(parts[1]);
                }
            });

            VBox root = new VBox(10);
            root.setPadding(new Insets(10));
            root.getChildren().addAll(nameField, ageField, buttons, listView);

            Scene scene = new Scene(root, 300, 400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("JavaFX Database Example");
            primaryStage.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());

        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO records (name, age) VALUES (?, ?)");
            statement.setString(1, name);
            statement.setInt(2, age);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new record was inserted successfully!");
                retrieveData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE records SET age = ? name = ? WHERE id =?");
            statement.setInt(1, age);
            statement.setString(2, name);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Record updated successfully!");
                retrieveData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteData() {
        String name = nameField.getText();

        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM records WHERE name = ?");
            statement.setString(1, name);

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Record deleted successfully!");
                retrieveData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retrieveData() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM records");

            data.clear();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                data.add(name + " - " + age);
            }

            listView.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
