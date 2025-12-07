package com.app.model;

public class User {
    private String userId;
    private String username;
    private String passwordHash;
    private String email;
    private String role; 
    private String department;
    private int clearanceLevel;

    // Default Constructor
    public User() {}

    // Getters and Setters

    public String  getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getClearanceLevel() { return clearanceLevel; }
    public void setClearanceLevel(int clearanceLevel) { this.clearanceLevel = clearanceLevel; }
}
