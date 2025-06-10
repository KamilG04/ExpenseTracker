package org.budget_manager.command;
import org.budget_manager.model.Transaction;

import java.util.List;
public class RemoveTransactionCommand implements ICommand {
    private final List<Transaction> transactions;
    private final Transaction transaction;

    public RemoveTransactionCommand(List<Transaction> transactions, Transaction transaction) {
        this.transactions = transactions;
        this.transaction = transaction;
    }

    @Override
    public void execute() {
        transactions.remove(transaction);
    }

    @Override
    public void undo() {
        transactions.add(transaction);
    }

    @Override
    public void redo() {
        execute();  // Reuse execute logic
    }
}
