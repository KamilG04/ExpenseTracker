package org.budget_manager.strategy;

import org.budget_manager.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LinearForecastStrategy implements IForecastStrategy {
    @Override
    public BigDecimal forecast(List<Transaction> historicalData, LocalDateTime startDate, LocalDateTime endDate) {
        if (historicalData.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Calculate average daily expense
        BigDecimal totalExpenses = historicalData.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long daysBetween = ChronoUnit.DAYS.between(
                historicalData.get(0).getDateTime(),
                historicalData.get(historicalData.size() - 1).getDateTime()
        );

        if (daysBetween == 0) {
            daysBetween = 1;
        }

        BigDecimal dailyAverage = totalExpenses.divide(
                new BigDecimal(daysBetween),
                2,
                BigDecimal.ROUND_HALF_UP
        );

        // Project for the forecast period
        long forecastDays = ChronoUnit.DAYS.between(startDate, endDate);
        return dailyAverage.multiply(new BigDecimal(forecastDays));
    }

    @Override
    public String getStrategyDescription() {
        return "Linear forecast based on historical daily average";
    }
}
