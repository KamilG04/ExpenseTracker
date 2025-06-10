package org.budget_manager.observer;


import org.budget_manager.model.Budget;

public class BudgetAlertObserver implements BudgetObserver {
    @Override
    public void update(Budget budget) {
        // Show alert in GUI when budget threshold is exceeded
    }
}