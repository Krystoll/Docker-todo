package com.example.todo.dto;

import com.example.todo.model.Task;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;


public class TaskRequest {

    @NotBlank(message = "Название задачи не может быть пустым")
    private String title;

    private String description;

    private Task.Priority priority = Task.Priority.MEDIUM;

    private LocalDate dueDate;

    private Task.Status status = Task.Status.TODO;

    // Getters / Setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Task.Priority getPriority() { return priority; }
    public void setPriority(Task.Priority priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Task.Status getStatus() { return status; }
    public void setStatus(Task.Status status) { this.status = status; }
}