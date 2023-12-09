package com.example.task;

import java.util.List;

public class LoggingTaskManager extends TaskManager {
    private TaskManager taskManager;

    public LoggingTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void addTask(Task task) {
        System.out.println("Adding task: " + task);
        taskManager.addTask(task);
    }

    @Override
    public void updateTask(Task task) {
        System.out.println("Updating task: " + task);
        taskManager.updateTask(task);
    }

    @Override
    public void deleteTask(Task task) {
        System.out.println("Deleting task: " + task);
        taskManager.deleteTask(task);
    }

    @Override
    public List<Task> getTasks() {
        System.out.println("Getting tasks");
        return taskManager.getTasks();
    }
}