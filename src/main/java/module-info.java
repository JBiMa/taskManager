module com.example.taskmanager {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.controllers to javafx.fxml;
    opens com.example.taskmanager to javafx.fxml;
    exports com.example.controllers to javafx.fxml;
    opens com.example.task to javafx.base;
    exports com.example.taskmanager;
}