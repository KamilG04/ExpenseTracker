package org.budget_manager.state;

import java.math.BigDecimal;

public class ExceededBudgetState implements IBudgetState {
    private final BudgetStateContext context;

    public ExceededBudgetState(BudgetStateContext context) {
        this.context = context;
    }

    @Override
    public void handleBudgetUpdate(BigDecimal currentAmount, BigDecimal limit) {
        if (currentAmount.compareTo(limit) <= 0) {
            context.setState(new WarningBudgetState(context));
        }
    }
}
