package com.example.user;

import com.example.task.Task;
import com.example.task.TaskObserver;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Worker extends User implements Serializable, TaskObserver {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Task> tasks;

    public Worker(String username, String password) {
        super(username, password);
        this.tasks = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }
    @Override
    public String toString() {
        return getUsername();
    }
    @Override
    public void update(Task task) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Task Assigned");
            alert.setHeaderText(null);
            alert.setContentText("A new task has been assigned to you: " + task.getTitle());
            alert.showAndWait();
        });
    }
}
