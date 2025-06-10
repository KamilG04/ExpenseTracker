package org.budget_manager.Builder;

import org.budget_manager.model.Report;
import org.budget_manager.model.Transaction;
import org.budget_manager.model.Category;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

public interface ReportBuilder {
    ReportBuilder setTransactions(List<Transaction> transactions);
    ReportBuilder setCategorySummary(Map<Category, BigDecimal> categorySummary);
    ReportBuilder setIncome(BigDecimal income);
    ReportBuilder setExpenses(BigDecimal expenses);
    ReportBuilder setBalance(BigDecimal balance);
    ReportBuilder setMonthlyIncome(BigDecimal monthlyIncome);
    ReportBuilder setTitle(String title);
    Report getResult();

    void reset();
}