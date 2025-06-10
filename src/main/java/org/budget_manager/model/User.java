package org.budget_manager.model;


import java.math.BigDecimal;

public class User {
    private Long id;
    private String username;
    private String email;
    private Budget budget;

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.budget = new Budget();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Budget getBudget() { return budget; }
    public void setBudget(Budget budget) { this.budget = budget; }


}

