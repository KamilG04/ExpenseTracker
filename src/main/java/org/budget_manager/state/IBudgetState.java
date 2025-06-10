package org.budget_manager.state;

import java.math.BigDecimal;

public interface IBudgetState {
    void handleBudgetUpdate(BigDecimal currentAmount, BigDecimal limit);
}