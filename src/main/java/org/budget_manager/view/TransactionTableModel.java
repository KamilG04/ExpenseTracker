package org.budget_manager.view;

import org.budget_manager.model.Transaction;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TransactionTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Date", "Category", "Description", "Amount", "Type"};
    private List<Transaction> transactions;

    public TransactionTableModel() {
        this.transactions = new ArrayList<>();
    }

    public void updateData(List<Transaction> newTransactions) {
        this.transactions = new ArrayList<>(newTransactions);
        fireTableDataChanged();
    }

    public Transaction getTransactionAt(int row) {
        return transactions.get(row);
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Transaction transaction = transactions.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> transaction.getDateTime().toString();
            case 1 -> transaction.getCategory().getDisplayName();
            case 2 -> transaction.getDescription();
            case 3 -> transaction.getAmount().toString();
            case 4 -> transaction.getType().toString();

            default -> null;
        };
    }
}