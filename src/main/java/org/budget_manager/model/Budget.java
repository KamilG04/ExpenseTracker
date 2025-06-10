package org.budget_manager.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;




import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Budget {
    private Long id;
    private List<Transaction> transactions;
    private Map<Category, BigDecimal> categoryLimits;
    private BigDecimal totalLimit;
    private BigDecimal monthlyIncome;

    public Budget() {
        this.transactions = new ArrayList<>();
        this.categoryLimits = new HashMap<>();
        this.totalLimit = BigDecimal.ZERO;
        this.monthlyIncome = BigDecimal.ZERO;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }
    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public void setCategoryLimit(Category category, BigDecimal limit) {
        categoryLimits.put(category, limit);
    }

    public BigDecimal getCategoryLimit(Category category) {
        return categoryLimits.getOrDefault(category, BigDecimal.ZERO);
    }

    public BigDecimal getCategoryTotal(Category category) {
        return transactions.stream()
                .filter(t -> t.getCategory() == category)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getCurrentTotal() {
         BigDecimal total=transactions.stream()
                .map(transaction -> {
                    if (transaction.getType() == Transaction.TransactionType.INCOME) {
                        return transaction.getAmount();
                    } else if (transaction.getType() == Transaction.TransactionType.EXPENSE) {
                        return transaction.getAmount().negate();
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
         total=total.add(monthlyIncome);
         return total;
    }
// ten chyba chujowy
//    public BigDecimal getCurrentTotal() {
//        BigDecimal total = BigDecimal.ZERO;
//        for (Transaction t : transactions) {
//            if (t.getType() == Transaction.TransactionType.INCOME) {
//                total = total.add(t.getAmount());
//            } else {
//                total = total.subtract(t.getAmount());
//            }
//        }
//        return total;
//    }
}