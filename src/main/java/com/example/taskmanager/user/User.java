package com.example.taskmanager.user;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Setter
    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Setter
    @Column(nullable = false, length = 255)
    private String fullName;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false, length = 50)
    private Set<Role> roles;

    @Setter
    @Column(nullable = false)
    private OffsetDateTime createdAt;

    public Long getId() { return id; }
    public String getEmail() { return email; }

    public String getPasswordHash() { return passwordHash; }

    public String getFullName() { return fullName; }

    public Set<Role> getRoles() { return roles; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
}
