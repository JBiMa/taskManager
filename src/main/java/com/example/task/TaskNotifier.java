package com.example.task;

public class TaskNotifier implements TaskObserver {
    @Override
    public void update(Task task) {
        // Code to execute when the state of the task changes
        System.out.println("Task " + task.getTitle() + " has been updated.");
    }
}