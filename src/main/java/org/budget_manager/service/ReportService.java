package org.budget_manager.service;

import org.budget_manager.model.Report;
import org.budget_manager.model.Transaction;
import org.budget_manager.Builder.Director;
import org.budget_manager.Builder.ReportBuilder;
import org.budget_manager.Builder.ReportBuilderImpl;

import java.math.BigDecimal;
import java.util.List;

public class ReportService {
    private final Director director;
    private final ReportBuilder builder;

    public ReportService() {
        this.builder = new ReportBuilderImpl();
        this.director = new Director(builder);
    }

    public Report generateSimpleReport(List<Transaction> transactions, BigDecimal monthlyIncome) {
        director.constructSimpleReport(transactions, monthlyIncome);
        return builder.getResult();
    }

    public Report generateDetailedReport(List<Transaction> transactions, BigDecimal monthlyIncome) {
        director.constructDetailedReport(transactions, monthlyIncome);
        return builder.getResult();
    }
}
