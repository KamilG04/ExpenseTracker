package org.budget_manager.state;

import java.math.BigDecimal;

public class BudgetStateContext {
    private IBudgetState currentState;
    private final BigDecimal budgetLimit;

    public BudgetStateContext(BigDecimal budgetLimit) {
        this.budgetLimit = budgetLimit;
        this.currentState = new NormalBudgetState(this);
    }

    public void setState(IBudgetState state) {
        this.currentState = state;
    }

    public void updateBudget(BigDecimal currentAmount) {
        currentState.handleBudgetUpdate(currentAmount, budgetLimit);
    }

    public BigDecimal getBudgetLimit() {
        return budgetLimit;
    }
}

