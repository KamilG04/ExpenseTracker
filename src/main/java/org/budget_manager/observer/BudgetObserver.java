package org.budget_manager.observer;

import org.budget_manager.model.Budget;

public interface BudgetObserver {
    void update(Budget budget);
}