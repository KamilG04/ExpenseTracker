package org.budget_manager.viewModel;

import org.budget_manager.model.Budget;
import org.budget_manager.model.Category;
import org.budget_manager.model.Transaction;
import org.budget_manager.observer.IBudgetObserver;
import org.budget_manager.state.BudgetStateContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BudgetViewModel extends AbstractViewModel {
    public final Budget budget;
    private final BudgetStateContext stateContext;
    private final Map<Category, BigDecimal> categoryLimits;

    public BudgetViewModel(Budget budget) {
        super();
        this.budget = budget;
        this.stateContext = new BudgetStateContext(budget.getTotalLimit());
        this.categoryLimits = new HashMap<>();
        initializeCategoryLimits();
    }

    private void initializeCategoryLimits() {
        for (Category category : Category.values()) {
            categoryLimits.put(category, BigDecimal.ZERO);
        }
    }

//    public void setMonthlyIncome(BigDecimal amount) {
//        budget.setTotalLimit(amount);
//        stateContext.updateBudget(budget.getCurrentTotal());
//        notifyObservers();
//    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        budget.setMonthlyIncome(monthlyIncome);
    }


    public void setCategoryLimit(Category category, BigDecimal limit) {
        categoryLimits.put(category, limit);
        budget.setCategoryLimit(category, limit);
        notifyObservers();
    }

    public void setTotalBudget(BigDecimal amount) {
        budget.setTotalLimit(amount);
        stateContext.updateBudget(budget.getCurrentTotal());
        notifyObservers();
    }


    public void addTransaction(Transaction transaction) {
        budget.addTransaction(transaction);
        stateContext.updateBudget(budget.getCurrentTotal());
        notifyObservers();
    }

    public void removeTransaction(Transaction transaction) {
        budget.removeTransaction(transaction);
        stateContext.updateBudget(budget.getCurrentTotal());
        notifyObservers();
    }

    public List<Transaction> getTransactions() {
        return budget.getTransactions();
    }

    public BigDecimal getCurrentTotal() {
        return budget.getCurrentTotal();
    }

    @Override
    protected void notifyObserver(IBudgetObserver observer) {
        observer.update(budget.getCurrentTotal(), budget.getTotalLimit());
    }
}
