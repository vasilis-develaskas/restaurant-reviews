package com.deve.restaurantreviews.user;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Locale;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String username;
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    protected User(){}

    public User(String email, String username, String passwordHash) {
        this.email = normalizeEmail(email);
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = Role.USER;
    }

    public void changeUsername(String newUsername) {
        this.username = newUsername;
    }

    public void changeEmail(String newEmail) {
        this.email = normalizeEmail(newEmail);
    }

    public void changePasswordHash(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
    }

    public void changeRole(Role newRole) {
        this.role = newRole;
    }

    public Boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    //-------------Getters

    public Long getId() {return id;}

    public String getPasswordHash() {return passwordHash;}

    public String getUsername() {return username;}

    public String getEmail() {return email;}

    public Role getRole() {return role;}

    public Long getVersion() {return version;}

    public Instant getUpdatedAt() {return updatedAt;}

    public Instant getCreatedAt() {return createdAt;}
}
