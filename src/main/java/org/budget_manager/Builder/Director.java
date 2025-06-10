package org.budget_manager.Builder;

import org.budget_manager.model.Transaction;
import org.budget_manager.model.Category;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Director {
    private final ReportBuilder builder;
    public Director(ReportBuilder builder) {
        this.builder = builder;
    }

    public void constructSimpleReport(List<Transaction> transactions, BigDecimal monthlyIncome) {
        BigDecimal income = calculateIncome(transactions).add(monthlyIncome);
        BigDecimal expenses = calculateExpenses(transactions);
        BigDecimal balance = income.subtract(expenses);
        Map<Category, BigDecimal> categorySummary = calculateCategorySummary(transactions);

        builder.setMonthlyIncome(monthlyIncome)
                .setIncome(income)
                .setExpenses(expenses)
                .setCategorySummary(categorySummary)
                .setBalance(balance)
                .setTitle("Simple Report");
    }

    public void constructDetailedReport(List<Transaction> transactions, BigDecimal monthlyIncome) {
        BigDecimal income = calculateIncome(transactions).add(monthlyIncome);
        BigDecimal expenses = calculateExpenses(transactions);
        BigDecimal balance = income.subtract(expenses);
        Map<Category, BigDecimal> categorySummary = calculateCategorySummary(transactions);

        builder.setTransactions(transactions)
                .setMonthlyIncome(monthlyIncome)
                .setIncome(income)
                .setExpenses(expenses)
                .setBalance(balance)
                .setCategorySummary(categorySummary)
                .setTitle("Detailed Report");
    }

    private BigDecimal calculateIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Map<Category, BigDecimal> calculateCategorySummary(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add
                        )
                ));
    }
}
