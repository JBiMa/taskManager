package com.example.taskmanager;


import com.example.controllers.UserController;
import com.example.database.DatabaseInit;
import com.example.database.DatabaseMOCK;
import com.example.database.DatabaseState;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Main extends Application {
    private static DatabaseMOCK database;
    private static DatabaseState databaseState;
    public static void main(String[] args) {
        database = new DatabaseMOCK();
        databaseState = DatabaseState.getInstance();
        if (databaseState.exists()) {
            database = databaseState.loadState();
        } else {
            DatabaseInit.initialize(database);
            database.saveState();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginView.fxml"));
        Parent root = fxmlLoader.load();
        UserController userController = fxmlLoader.getController();
        userController.setDatabase(database);
        // Load the state of the database from a file
        database.loadState();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}