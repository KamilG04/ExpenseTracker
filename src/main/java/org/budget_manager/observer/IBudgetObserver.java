package org.budget_manager.observer;


import java.math.BigDecimal;

public interface IBudgetObserver {
    void update(BigDecimal current, BigDecimal limit);
}
