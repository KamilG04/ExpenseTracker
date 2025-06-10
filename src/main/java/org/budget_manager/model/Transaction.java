package org.budget_manager.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class Transaction {
    private Long id;
    private BigDecimal amount;
    private Category category;
    private String description;
    private LocalDateTime dateTime;
    private TransactionType type;

    public enum TransactionType {
        INCOME, EXPENSE
    }

    public Transaction(BigDecimal amount, Category category, String description, TransactionType type) {
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.dateTime = LocalDateTime.now();
        this.type = type;


    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public LocalDateTime getDateTime() { return dateTime; }
    public TransactionType getType() { return type; }
}