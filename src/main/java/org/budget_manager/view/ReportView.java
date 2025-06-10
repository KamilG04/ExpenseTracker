package org.budget_manager.view;

import org.budget_manager.service.ReportService;
import org.budget_manager.model.Budget;
import org.budget_manager.model.Category;
import org.budget_manager.model.Report;
import org.budget_manager.model.Transaction;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReportView extends JFrame {
    private final ReportService reportService;
    private JTextArea reportArea;
    public Budget budget;

    public ReportView(ReportService reportService) {
        this.reportService = reportService;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Budget Report");
        setSize(600, 400);
        setLayout(new BorderLayout());

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton simpleReportButton = new JButton("Generate Simple Report");
        JButton detailedReportButton = new JButton("Generate Detailed Report");

        simpleReportButton.addActionListener(e -> generateSimpleReport());
        detailedReportButton.addActionListener(e -> generateDetailedReport());

        buttonPanel.add(simpleReportButton);
        buttonPanel.add(detailedReportButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void generateSimpleReport() {
        Report report = reportService.generateSimpleReport(budget.getTransactions(),budget.getMonthlyIncome());
        displayReport(report);
    }

    private void generateDetailedReport() {
        Report report = reportService.generateDetailedReport(budget.getTransactions(),budget.getMonthlyIncome());
        displayReport(report);
    }

    private void displayReport(Report report) {
        StringBuilder sb = new StringBuilder();
        sb.append("Report: ").append(report.getTitle()).append("\n\n");
        sb.append("Monthly Income: $").append(report.monthlyIncome).append("\n"); //ktos robi gettery
        sb.append("Total Income: $").append(report.getTotalIncome()).append("\n");
        sb.append("Total Expenses: $").append(report.getTotalExpenses()).append("\n");
        sb.append("Balance: $").append(report.getBalance()).append("\n\n");

        if (!report.isCategorySummaryEmpty()) {
            sb.append("Category Summary:\n");
            for (Map.Entry<Category, BigDecimal> entry : report.getCategorySummary().entrySet()) {
                sb.append(entry.getKey().getDisplayName())
                        .append(": $")
                        .append(entry.getValue())
                        .append("\n");
            }
        }
        if (!report.isTransactionsEmpty()) {
            sb.append("\nTransactions:\n");
            for (Transaction t : report.getTransactions()) {
                sb.append(t.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                        .append(" | ")
                        .append(t.getCategory().getDisplayName())
                        .append(" | $")
                        .append(t.getAmount())
                        .append(" | ")
                        .append(t.getDescription())
                        .append("\n");
            }
        }
        reportArea.setText(sb.toString());
    }
}
