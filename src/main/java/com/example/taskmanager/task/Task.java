package com.example.taskmanager.task;

import com.example.taskmanager.user.User;
import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 255)
    private String title;

    @Setter
    @Column(columnDefinition = "text")
    private String description;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TaskStatus status;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TaskPriority priority;

    @Setter
    private LocalDate dueDate;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Setter
    @Column(nullable = false)
    private OffsetDateTime createdAt;

    @Setter
    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    public Long getId() { return id; }
    public String getTitle() { return title; }

    public String getDescription() { return description; }

    public TaskStatus getStatus() { return status; }

    public TaskPriority getPriority() { return priority; }

    public LocalDate getDueDate() { return dueDate; }

    public User getOwner() { return owner; }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
