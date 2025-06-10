package org.budget_manager.command;

import org.budget_manager.model.Category;
import org.budget_manager.model.Transaction;

import java.math.BigDecimal;

public class EditTransactionCommand implements ICommand {
    private final Transaction transaction;
    private final BigDecimal oldAmount;
    private final Category oldCategory;
    private final String oldDescription;
    private final Transaction.TransactionType oldType;
    private final BigDecimal newAmount;
    private final Category newCategory;
    private final String newDescription;
    private final Transaction.TransactionType newType;

    public EditTransactionCommand(Transaction transaction,
                                  BigDecimal newAmount,
                                  Category newCategory,
                                  String newDescription,
                                  Transaction.TransactionType newType) {
        this.transaction = transaction;
        this.oldAmount = transaction.getAmount();
        this.oldCategory = transaction.getCategory();
        this.oldDescription = transaction.getDescription();
        this.oldType = transaction.getType();
        this.newAmount = newAmount;
        this.newCategory = newCategory;
        this.newDescription = newDescription;
        this.newType = newType;
    }

    @Override
    public void execute() {
        transaction.setAmount(newAmount);
        transaction.setCategory(newCategory);
        transaction.setDescription(newDescription);
        transaction.setType(newType);
    }

    @Override
    public void undo() {
        transaction.setAmount(oldAmount);
        transaction.setCategory(oldCategory);
        transaction.setDescription(oldDescription);
        transaction.setType(oldType);
    }

    @Override
    public void redo() {
        execute();  // Reuse execute logic
    }
}