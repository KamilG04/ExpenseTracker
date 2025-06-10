package org.budget_manager.viewModel;

import org.budget_manager.Facade.CsvFacade;  // <-- Upewnij się, że importujesz właściwy pakiet
import org.budget_manager.model.Transaction;
import org.budget_manager.model.Category;
import org.budget_manager.command.ICommand;
import org.budget_manager.command.AddTransactionCommand;
import org.budget_manager.command.RemoveTransactionCommand;
import org.budget_manager.observer.IBudgetObserver;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionViewModel extends AbstractViewModel {
    private final Stack<ICommand> undoStack;
    private final Stack<ICommand> redoStack;
    private final List<Transaction> transactions;
    private final Map<Category, BigDecimal> categoryTotals;

    private final CsvFacade csvFacade;

    public TransactionViewModel() {
        super();
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
        this.transactions = new ArrayList<>();
        this.categoryTotals = new HashMap<>();

        this.csvFacade = new CsvFacade();
    }

    public void addTransaction(Transaction transaction) {
        ICommand command = new AddTransactionCommand(transactions, transaction);
        executeCommand(command);
        updateCategoryTotals(transaction, true);
        notifyObservers();
    }

    public void removeTransaction(Transaction transaction) {
        ICommand command = new RemoveTransactionCommand(transactions, transaction);
        executeCommand(command);
        updateCategoryTotals(transaction, false);
        notifyObservers();
    }

    public void executeCommand(ICommand command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            ICommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            recalculateCategoryTotals();
            notifyObservers();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            ICommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            recalculateCategoryTotals();
            notifyObservers();
        }
    }

    private void updateCategoryTotals(Transaction transaction, boolean isAdd) {
        Category category = transaction.getCategory();
        BigDecimal currentTotal = categoryTotals.getOrDefault(category, BigDecimal.ZERO);

        if (isAdd) {
            categoryTotals.put(category, currentTotal.add(transaction.getAmount()));
        } else {
            categoryTotals.put(category, currentTotal.subtract(transaction.getAmount()));
        }
    }

    private void recalculateCategoryTotals() {
        categoryTotals.clear();
        for (Transaction transaction : transactions) {
            updateCategoryTotals(transaction, true);
        }
    }

    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactions.stream()
                .filter(t -> t.getCategory() == category)
                .toList();
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactions.stream()
                .filter(t -> !t.getDateTime().isBefore(start) && !t.getDateTime().isAfter(end))
                .toList();
    }

    public BigDecimal getCategoryTotal(Category category) {
        return categoryTotals.getOrDefault(category, BigDecimal.ZERO);
    }

    public Map<Category, BigDecimal> getAllCategoryTotals() {
        return new HashMap<>(categoryTotals);
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    @Override
    protected void notifyObserver(IBudgetObserver observer) {
        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        observer.update(total, null);
    }
    /**
     * Importuje transakcje z pliku CSV przy pomocy CsvFacade.
     * W tym miejscu wywołujemy 'importCsv(...)', a następnie
     * tworzymy Transaction z każdej linijki CSV i dodajemy do listy.
     */
    public void importTransactionsFromCsv(String filePath) throws IOException {
        // Odczyt z pliku za pomocą fasady
        List<String[]> rows = csvFacade.importCsv(filePath);

        // Z każdej tablicy Stringów tworzymy Transaction i dodajemy
        for (String[] row : rows) {
            Transaction transaction = parseTransaction(row);
            addTransaction(transaction);
        }
    }

    /**
     * Eksportuje transakcje do pliku CSV przy pomocy CsvFacade.
     * Najpierw konwertujemy Transaction -> tablica Stringów (wiersz CSV),
     * następnie wywołujemy 'exportCsv(...)' w fasadzie.
     */
    public void exportTransactionsToCsv(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        for (Transaction t : transactions) {
            rows.add(formatTransaction(t));
        }
        csvFacade.exportCsv(filePath, rows);
    }

    private Transaction parseTransaction(String[] columns) {
        if (columns.length < 4) {
            throw new IllegalArgumentException("Niepoprawny format CSV - za mało kolumn.");
        }

        Category category = Category.valueOf(columns[0]);
        BigDecimal amount = new BigDecimal(columns[1]);
        LocalDateTime dateTime = LocalDateTime.parse(columns[2]);
        String description = columns[3];

        // Tutaj zależy od Twojego konstruktora Transaction:
        return new Transaction(amount, Category.FOOD, description, Transaction.TransactionType.EXPENSE);
    }

    /**
     * Przykładowe mapowanie Transaction -> dane CSV.
     */
    private String[] formatTransaction(Transaction t) {
        return new String[] {
                t.getCategory().name(),
                t.getAmount().toString(),
                t.getDateTime().toString(),
                t.getDescription() != null ? t.getDescription() : ""
        };
    }
}
