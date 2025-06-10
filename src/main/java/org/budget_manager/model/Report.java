package org.budget_manager.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Report {
    private Long id;
    private String title;
    private LocalDateTime generationDate;
    private List<Transaction> transactions;
    private Map<Category, BigDecimal> categorySummary;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal balance;
    private String summary;
    public BigDecimal monthlyIncome;
    // Getters and setters
    public void setTitle(String title) { this.title = title; }
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public void setCategorySummary(Map<Category, BigDecimal> categorySummary) {
        this.categorySummary = categorySummary;
    }
    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }
    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

    // Getters
    public String getTitle() { return title; }
    public List<Transaction> getTransactions() { return transactions; }
    public Map<Category, BigDecimal> getCategorySummary() { return categorySummary; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public BigDecimal getBalance() { return balance; }
    public String getSummary() { return summary; }


    public boolean isCategorySummaryEmpty() {
        return categorySummary == null || categorySummary.isEmpty();
    }

    public boolean isTransactionsEmpty() {
        return transactions == null || transactions.isEmpty();
    }

}
