package org.budget_manager.strategy;

import org.budget_manager.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;

public class HistoricalForecastStrategy implements IForecastStrategy {
    @Override
    public BigDecimal forecast(List<Transaction> historicalData, LocalDateTime startDate, LocalDateTime endDate) {
        if (historicalData.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Group transactions by month and calculate monthly averages
        Map<Integer, BigDecimal> monthlyAverages = historicalData.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDateTime().getMonthValue(),
                        Collectors.mapping(
                                Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        // Calculate weighted average giving more importance to recent months
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal weightedSum = BigDecimal.ZERO;
        int currentMonth = LocalDateTime.now().getMonthValue();

        for (Map.Entry<Integer, BigDecimal> entry : monthlyAverages.entrySet()) {
            int monthDiff = Math.abs(currentMonth - entry.getKey());
            BigDecimal weight = new BigDecimal("1.0").divide(new BigDecimal(monthDiff + 1), 2, BigDecimal.ROUND_HALF_UP);
            weightedSum = weightedSum.add(entry.getValue().multiply(weight));
            totalWeight = totalWeight.add(weight);
        }

        BigDecimal monthlyForecast = weightedSum.divide(totalWeight, 2, BigDecimal.ROUND_HALF_UP);
        long monthsBetween = ChronoUnit.MONTHS.between(startDate, endDate);

        return monthlyForecast.multiply(new BigDecimal(monthsBetween));
    }

    @Override
    public String getStrategyDescription() {
        return "Historical forecast based on weighted monthly averages";
    }
}
