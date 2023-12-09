package com.example.task;

import com.example.user.User;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID id;
    private String title;
    private String description;
    private double durationTime;
    private Date deadline;
    private int priority;
    private boolean isDone;
    private User assignedUser;
    private List<TaskObserver> observers = new ArrayList<>();

    public Task(String title, String description, double durationTime, Date deadline, int priority) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.durationTime = durationTime;
        this.deadline = deadline;
        this.priority = priority;
        this.isDone = false;
        this.assignedUser = null;
    }
    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String workerName = assignedUser != null ? assignedUser.getUsername() : "Not assigned";
        return "Title: " + title + ", Description: " + description + ", Worker: " + workerName
                + ", Deadline: " + dateFormat.format(deadline) + ", Is Done: " + (isDone ? "Yes" : "No");
    }
    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() { return description; }

    public double getDurationTime() {
        return durationTime;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isDone() {
        return isDone;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDurationTime(double durationTime) {
        this.durationTime = durationTime;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (TaskObserver observer : observers) {
            observer.update(this);
        }
    }
}
