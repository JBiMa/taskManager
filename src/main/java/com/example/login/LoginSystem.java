package com.example.login;

import com.example.database.DatabaseMOCK;
import com.example.task.Task;
import com.example.user.User;
import com.example.user.Worker;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.List;

public class LoginSystem {
    private DatabaseMOCK database;

    public LoginSystem(DatabaseMOCK database) {
        this.database = database;
    }

    public User validateUserCredentials(String username, String password) {
        User user = database.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            if (user instanceof Worker) {
                Worker worker = (Worker) user;
                List<Task> newTasks = database.getTasksForUser(worker);
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("New Tasks");
                    alert.setHeaderText(null);
                    alert.setContentText("You have " + newTasks.size() + " new tasks.");
                    alert.showAndWait();
                });
            }
            return user;
        }
        return null;
    }
}