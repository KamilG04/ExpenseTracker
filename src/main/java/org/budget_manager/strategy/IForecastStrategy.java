package org.budget_manager.strategy;

import org.budget_manager.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IForecastStrategy {
    BigDecimal forecast(List<Transaction> historicalData, LocalDateTime startDate, LocalDateTime endDate);
    String getStrategyDescription();
}