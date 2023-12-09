package com.example.database;

import com.example.task.Task;
import com.example.user.User;

import java.io.Serializable;
import java.util.List;

public class Memento implements Serializable {
    private static final long serialVersionUID = 1L;
    private final List<User> users;
    private final List<Task> tasks;

    public Memento(List<User> users, List<Task> tasks) {
        this.users = users;
        this.tasks = tasks;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}