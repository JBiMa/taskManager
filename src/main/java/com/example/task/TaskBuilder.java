package com.example.task;

import java.util.Date;

public class TaskBuilder {
    private String title;
    private String description;
    private double durationTime;
    private Date deadline;
    private int priority;

    public TaskBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder setDurationTime(double durationTime) {
        this.durationTime = durationTime;
        return this;
    }

    public TaskBuilder setDeadline(Date deadline) {
        this.deadline = deadline;
        return this;
    }

    public TaskBuilder setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Task build() {
        return new Task(title, description, durationTime, deadline, priority);
    }
}