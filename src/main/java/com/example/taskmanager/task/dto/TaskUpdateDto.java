package com.example.taskmanager.task.dto;

import com.example.taskmanager.task.TaskPriority;
import com.example.taskmanager.task.TaskStatus;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class TaskUpdateDto {
    @Size(min = 1, max = 255)
    private String title;

    @Size(max = 4000)
    private String description;

    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}

