package org.budget_manager.state;

import java.math.BigDecimal;

public class NormalBudgetState implements IBudgetState {
    private final BudgetStateContext context;

    public NormalBudgetState(BudgetStateContext context) {
        this.context = context;
    }

    @Override
    public void handleBudgetUpdate(BigDecimal currentAmount, BigDecimal limit) {
        if (currentAmount.compareTo(limit.multiply(new BigDecimal("0.8"))) > 0) {
            context.setState(new WarningBudgetState(context));
        }
    }
}