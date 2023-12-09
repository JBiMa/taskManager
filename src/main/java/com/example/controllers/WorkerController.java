package com.example.controllers;

import com.example.database.DatabaseMOCK;
import com.example.task.Task;
import com.example.taskmanager.HelloApplication;
import com.example.user.Worker;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Date;

public class WorkerController {
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, Date> deadlineColumn;
    @FXML
    private TableColumn<Task, Boolean> isDoneColumn;
    @FXML
    private TableColumn<Task, Void> actionColumn;
    @FXML
    private TableColumn<Task, Integer> priorityColumn;

    private DatabaseMOCK database;
    private Worker worker;

    public void setWorker(Worker worker) {
        this.worker = worker;
        updateTaskTableView();
    }

    public void setDatabase(DatabaseMOCK database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        deadlineColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
        isDoneColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDone()));
        priorityColumn.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getPriority()).asObject());
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button doneButton = new Button("Done");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    doneButton.setOnAction(event -> {
                        Task task = getTableView().getItems().get(getIndex());
                        task.setDone(true);
                        database.updateTask(task); // Update the task in the database
                        updateTaskTableView();
                        database.saveState();
                    });
                    setGraphic(doneButton);
                }

            }
        });
    }

    private void updateTaskTableView() {
        ObservableList<Task> taskList = FXCollections.observableArrayList();
        taskList.addAll(worker.getTasks());

        // Remove deleted tasks from the taskList
        taskList.removeIf(task -> !database.getTasks().contains(task));

        taskTableView.setItems(taskList);
        taskTableView.refresh();
    }

    @FXML
    protected void handleLogoutButtonAction() {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginView.fxml"));
        try {
            Parent root = fxmlLoader.load();
            UserController controller = fxmlLoader.getController();
            controller.setDatabase(database);
            Platform.runLater(() -> {
                Stage stage = (Stage) taskTableView.getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/com/example/taskmanager/modernStyle.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}