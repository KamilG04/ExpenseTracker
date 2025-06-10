package org.budget_manager.strategy;

import org.budget_manager.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ExpenseForecast {
    private IForecastStrategy strategy;

    public ExpenseForecast(IForecastStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(IForecastStrategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal calculateForecast(List<Transaction> historicalData,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate) {
        return strategy.forecast(historicalData, startDate, endDate);
    }

    public String getStrategyDescription() {
        return strategy.getStrategyDescription();
    }
}