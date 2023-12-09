package com.example.database;

import com.example.task.Task;
import com.example.task.TaskBuilder;
import com.example.user.Manager;
import com.example.user.Worker;

import java.util.Date;

public class DatabaseInit {
    public static void initialize(DatabaseMOCK database) {
        Manager manager = new Manager("m1", "q1");
        database.addUser(manager);
        Worker worker1 = new Worker("w1", "q1");
        Worker worker2 = new Worker("w2", "q1");
        database.addUser(worker1);
        database.addUser(worker2);
        Task task1 = new TaskBuilder()
                .setTitle("Task1")
                .setDescription("This is task 1")
                .setDurationTime(2.0)
                .setDeadline(new Date())
                .setPriority(1)
                .build();
        Task task2 = new TaskBuilder()
                .setTitle("Task2")
                .setDescription("This is task 2")
                .setDurationTime(3.0)
                .setDeadline(new Date())
                .setPriority(4)
                .build();
        manager.assignTask(task1, worker1);
        manager.assignTask(task2, worker2);
        database.addTask(task1);
        database.addTask(task2);
    }
}