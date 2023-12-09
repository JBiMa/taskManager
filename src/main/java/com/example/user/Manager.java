package com.example.user;

import com.example.task.Task;
import com.example.task.TaskBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class Manager extends User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    public Manager(String username, String password) {
        super(username, password);
    }

    public Task createTask(String title, String description, double durationTime, Date deadline, int priority) {
        return new TaskBuilder()
                .setTitle(title)
                .setDescription(description)
                .setDurationTime(durationTime)
                .setDeadline(deadline)
                .setPriority(priority)
                .build();
    }

    public void assignTask(Task task, Worker worker) {
        // Implement the logic to assign a task to a worker
        task.setAssignedUser(worker);
        worker.addTask(task);
        task.addObserver(worker);
        task.notifyObservers();
    }
}