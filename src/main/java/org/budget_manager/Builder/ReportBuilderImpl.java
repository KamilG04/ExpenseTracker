package org.budget_manager.Builder;

import org.budget_manager.model.Report;
import org.budget_manager.model.Transaction;
import org.budget_manager.model.Category;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;

public class ReportBuilderImpl implements ReportBuilder {
    private Report report;

    public ReportBuilderImpl() {
        reset();
    }

    @Override
    public void reset() {
        report = new Report();
    }

    @Override
    public ReportBuilder setTransactions(List<Transaction> transactions) {
        report.setTransactions(transactions);
        return this;
    }

    @Override
    public ReportBuilder setCategorySummary(Map<Category, BigDecimal> categorySummary) {
        report.setCategorySummary(categorySummary);
        return this;
    }

    @Override
    public ReportBuilder setIncome(BigDecimal income) {
        report.setTotalIncome(income);
        return this;
    }

    @Override
    public ReportBuilder setExpenses(BigDecimal expenses) {
        report.setTotalExpenses(expenses);
        return this;
    }

    @Override
    public ReportBuilder setBalance(BigDecimal balance) {
        report.setBalance(balance);
        return this;
    }

    @Override
    public ReportBuilder setMonthlyIncome(BigDecimal monthlyIncome) {
        report.monthlyIncome=monthlyIncome;
        return this;
    }

    @Override
    public Report getResult() {
        Report completedReport = report;
        reset();
        return completedReport;
    }

    @Override
    public ReportBuilder setTitle(String title) {
        report.setTitle(title);
        return this;
    }
}
