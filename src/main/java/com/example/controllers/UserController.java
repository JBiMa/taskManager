package com.example.controllers;

import com.example.login.LoginSystem;
import com.example.database.DatabaseMOCK;
import com.example.taskmanager.HelloApplication;
import com.example.user.Manager;
import com.example.user.User;
import com.example.user.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UserController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label actiontarget;

    private LoginSystem loginSystem;
    private DatabaseMOCK database;

    public UserController() {
    }

    public void setDatabase(DatabaseMOCK database) {
        this.database = database;
        this.loginSystem = new LoginSystem(database);
    }

    @FXML
    protected void handleSubmitButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = loginSystem.validateUserCredentials(username, password);
        if (user != null) {
            actiontarget.setText("Sign in button pressed");
            Stage stage = (Stage) actiontarget.getScene().getWindow();
            FXMLLoader fxmlLoader;
            if (user.getType().equals("Manager")) {
                // Switch to the Manager view
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ManagerView.fxml"));
            } else {
                // Switch to the Worker view
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("WorkerView.fxml"));
            }
            try {
                Parent root = fxmlLoader.load();
                if (user.getType().equals("Manager")) {
                    ManagerController controller = fxmlLoader.getController();
                    controller.setDatabase(database);  // Add this line
                    controller.setManager((Manager) user);
                    controller.postInitialize();
                } else {
                    WorkerController controller = fxmlLoader.getController();
                    controller.setDatabase(database);  // Add this line
                    controller.setWorker((Worker) user);
                }
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/com/example/taskmanager/loginStyle.css").toExternalForm());
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            actiontarget.setText("Sign in failed");
        }
    }

}