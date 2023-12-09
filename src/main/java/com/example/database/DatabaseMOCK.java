package com.example.database;

import com.example.task.Task;
import com.example.user.User;
import com.example.user.Worker;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMOCK implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<User> users;
    private List<Task> tasks;

    public DatabaseMOCK() {
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
        loadState();
    }

    public void addUser(User user) {
        this.users.add(user);
        saveState();
    }

    public void addTask(Task task) {
        this.tasks.add(task);
        saveState();
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public List<Worker> getWorkers() {
        List<Worker> workers = new ArrayList<>();
        for (User user : users) {
            if (user.getType().equals("Worker")) {
                workers.add((Worker) user);
            }
        }
        return workers;
    }
    public List<Task> getTasksForUser(User user) {
        List<Task> userTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getAssignedUser() != null && task.getAssignedUser().getUsername().equals(user.getUsername())) {
                userTasks.add(task);
            }
        }
        return userTasks;
    }
    public List<Task> getTasks() {
        return tasks;
    }

    public Memento saveToMemento() {
        return new Memento(new ArrayList<>(users), new ArrayList<>(tasks));
    }

    public void restoreFromMemento(Memento memento) {
        users = memento.getUsers();
        tasks = memento.getTasks();
    }

    public void saveState() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("databaseState.ser"))) {
            oos.writeObject(saveToMemento());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadState() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("databaseState.ser"))) {
            Memento memento = (Memento) ois.readObject();
            restoreFromMemento(memento);
        } catch (FileNotFoundException e) {
            System.err.println("File databaseState.ser not found. The database will be initialized with default values.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task task) {
        int index = tasks.indexOf(task);
        if (index != -1) {
            tasks.set(index, task);
        }
        saveState();
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
        saveState();
    }
}