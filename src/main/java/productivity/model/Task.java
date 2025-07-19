package main.java.productivity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String title;
    private String description;
    private TaskPriority priority;
    @JsonIgnore
    private LocalDateTime createdAt;

    public Task() {
        this.createdAt = LocalDateTime.now();
        this.priority = TaskPriority.MEDIUM;
    }

    public Task(String title, String description, TaskPriority priority) {
        this();
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskPriority getPriority() { return priority; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }

    public String getFormattedDate() {
        return createdAt.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
    }
    public String getCreatedAtString() {
        return createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public void setCreatedAtString(String dateString) {
        this.createdAt = LocalDateTime.parse(dateString);
    }
}