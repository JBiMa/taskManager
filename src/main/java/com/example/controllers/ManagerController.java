package com.example.controllers;

import com.example.database.DatabaseMOCK;
import com.example.task.Task;
import com.example.task.TaskBuilder;
import com.example.taskmanager.HelloApplication;
import com.example.user.Manager;
import com.example.user.User;
import com.example.user.Worker;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class ManagerController {
    @FXML
    private ComboBox<Worker> workerComboBox;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField durationTimeField;
    @FXML
    private DatePicker deadlineDatePicker;
    @FXML
    private TextField deadlineTimePicker;
    @FXML
    private TextField priorityField;
    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> workerColumn;
    @FXML
    private TableColumn<Task, Date> deadlineColumn;
    @FXML
    private TableColumn<Task, Boolean> isDoneColumn;
    @FXML
    private TableColumn<Task, Void> deleteColumn;
    @FXML
    private TableColumn<Task, Void> modifyColumn;

    private Manager manager;
    private DatabaseMOCK database;

    public void setManager(Manager manager) {
        this.manager = manager;
        updateTaskTableView();
    }

    public void setDatabase(DatabaseMOCK database) {
        this.database = database;
    }

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        workerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAssignedUser().getUsername()));
        deadlineColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
        isDoneColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDone()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    deleteButton.setOnAction(event -> {
                        Task task = getTableView().getItems().get(getIndex());
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you sure you want to delete this task?");

                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                database.deleteTask(task);
                                updateTaskTableView();
                            }
                        });
                    });
                    setGraphic(deleteButton);
                }
            }
        });
        modifyColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifyButton = new Button("Modify");
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    modifyButton.setOnAction(event -> {
                        Task task = getTableView().getItems().get(getIndex());
                        // Create a new dialog
                        Dialog<Task> dialog = new Dialog<>();
                        dialog.setTitle("Modify Task");
                        dialog.setHeaderText("Modify the task details");
                        // Create labels and fields for the task properties
                        Label titleLabel = new Label("Title:");
                        TextField titleField = new TextField(task.getTitle());
                        Label descriptionLabel = new Label("Description:");
                        TextField descriptionField = new TextField(task.getDescription());
                        Label durationTimeLabel = new Label("Duration Time:");
                        TextField durationTimeField = new TextField(String.valueOf(task.getDurationTime()));
                        Label deadlineDateLabel = new Label("Deadline Date:");
                        DatePicker deadlineDatePicker = new DatePicker(task.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        Label deadlineTimeLabel = new Label("Deadline Time:");
                        TextField deadlineTimePicker = new TextField(task.getDeadline().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString());
                        Label priorityLabel = new Label("Priority:");
                        TextField priorityField = new TextField(String.valueOf(task.getPriority()));
                        // Layout the labels and fields in a grid pane
                        GridPane grid = new GridPane();
                        grid.add(titleLabel, 1, 1);
                        grid.add(titleField, 2, 1);
                        grid.add(descriptionLabel, 1, 2);
                        grid.add(descriptionField, 2, 2);
                        grid.add(durationTimeLabel, 1, 3);
                        grid.add(durationTimeField, 2, 3);
                        grid.add(deadlineDateLabel, 1, 4);
                        grid.add(deadlineDatePicker, 2, 4);
                        grid.add(deadlineTimeLabel, 1, 5);
                        grid.add(deadlineTimePicker, 2, 5);
                        grid.add(priorityLabel, 1, 6);
                        grid.add(priorityField, 2, 6);
                        dialog.getDialogPane().setContent(grid);
                        // Add a "Save" button to the dialog
                        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

                        // Handle the "Save" button click
                        dialog.setResultConverter(buttonType -> {
                            if (buttonType == saveButtonType) {
                                task.setTitle(titleField.getText());
                                task.setDescription(descriptionField.getText());
                                task.setDurationTime(Double.parseDouble(durationTimeField.getText()));
                                LocalDate deadlineDate = deadlineDatePicker.getValue();
                                LocalTime deadlineTime = LocalTime.parse(deadlineTimePicker.getText());
                                LocalDateTime deadlineDateTime = LocalDateTime.of(deadlineDate, deadlineTime);
                                task.setDeadline(Date.from(deadlineDateTime.atZone(ZoneId.systemDefault()).toInstant()));
                                task.setPriority(Integer.parseInt(priorityField.getText()));
                                return task;
                            }
                            return null;
                        });

                        dialog.showAndWait();
                        updateTaskTableView();
                    });
                    setGraphic(modifyButton);
                }
            }
        });

    }
    public void postInitialize() {
        workerComboBox.setItems(FXCollections.observableArrayList(database.getWorkers()));
    }
    @FXML
    protected void handleCreateTaskButtonAction() {
        if (!(workerComboBox.getValue() instanceof Worker)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a worker.");
            alert.showAndWait();
            return;
        }
        Worker selectedWorker = workerComboBox.getValue();
        String workerUsername = selectedWorker.getUsername();
        Worker worker = (Worker) database.getUser(workerUsername);
        if (worker != null) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            if (durationTimeField.getText().isEmpty() || priorityField.getText().isEmpty() || deadlineDatePicker.getValue() == null || deadlineTimePicker.getText().isEmpty() || title.isEmpty() || description.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the fields.");
                alert.showAndWait();
                return;
            }
            double durationTime;
            try {
                durationTime = Double.parseDouble(durationTimeField.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Duration time should be a number.");
                alert.showAndWait();
                return;
            }
            LocalDate deadlineDate;
            try {
                deadlineDate = deadlineDatePicker.getValue();
            } catch (DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Date should be in format YYYY-MM-DD.");
                alert.showAndWait();
                return;
            }
            LocalTime deadlineTime;
            try {
                deadlineTime = LocalTime.parse(deadlineTimePicker.getText());
            } catch (DateTimeParseException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Time should be in format HH:MM.");
                alert.showAndWait();
                return;
            }
            LocalDateTime deadlineDateTime = LocalDateTime.of(deadlineDate, deadlineTime);
            Date deadline = Date.from(deadlineDateTime.atZone(ZoneId.systemDefault()).toInstant());
            int priority;
            try {
                priority = Integer.parseInt(priorityField.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Priority should be a number.");
                alert.showAndWait();
                return;
            }

            Task task = manager.createTask(title, description, durationTime, deadline, priority);
            manager.assignTask(task, worker);
            database.addTask(task);
            updateTaskTableView();
            clearFields();
        }
    }
    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        durationTimeField.clear();
        deadlineDatePicker.setValue(null);
        deadlineTimePicker.clear();
        priorityField.clear();
    }
    @FXML
    protected void handleViewAllTasksButtonAction() {
        updateTaskTableView();
    }

    private void updateTaskTableView() {
        taskTableView.setItems(FXCollections.observableArrayList(database.getTasks()));
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