package org.budget_manager.view;

import org.budget_manager.service.ReportService;
import org.budget_manager.command.EditTransactionCommand;
import org.budget_manager.viewModel.*;
import org.budget_manager.model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class HomeView extends JFrame {
    private final CategoryViewModel categoryViewModel;
    private final TransactionViewModel transactionViewModel;
    private final BudgetViewModel budgetViewModel;
    private final JTable transactionTable;
    private final TransactionTableModel tableModel;

    private JPanel limitsPanel;
    private Map<Category, JLabel> categoryLimitLabels;
    private Map<Category, BigDecimal> categoryLimits;

    private JComboBox<Category> categoryComboBox;
    private JTextField amountField;
    private JTextField descriptionField;
    private JTextField monthlyIncomeField;
    private JLabel balanceLabel;
    private JButton addButton;
    private JButton editButton;
    private JButton undoButton;
    private JButton redoButton;

    private boolean budgetSet = false;

    public HomeView() {
        // Initialize controllers
        Budget budget = new Budget();

        categoryLimitLabels = new HashMap<>();
        categoryLimits = new HashMap<>();

        this.budgetViewModel = new BudgetViewModel(budget);
        this.categoryViewModel = new CategoryViewModel();
        this.transactionViewModel = new TransactionViewModel();

        // Initialize table model and components
        this.tableModel = new TransactionTableModel();
        this.transactionTable = new JTable(tableModel);

        // Set up UI
        initializeUI();
        setupTableActions();

        // Initially disable transaction controls until monthly income is set
        toggleTransactionControls(false);
        updateDisplay();
    }

    private void initializeUI() {
        setTitle("Budget Manager");
        setSize(1200, 700); // Zwiększona szerokość dla panelu limitów
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Utworzenie głównego panelu z podziałem
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Panel prawy (istniejące komponenty)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(createTopPanel(), BorderLayout.NORTH);
        rightPanel.add(createCenterPanel(), BorderLayout.CENTER);

        // Panel lewy (limity kategorii)
        createLimitsPanel();

        // Dodanie paneli do splitPane
        splitPane.setLeftComponent(createLimitsPanel());
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(250); // Szerokość lewego panelu

        add(splitPane, BorderLayout.CENTER);
        setupMenuBar();
        setLocationRelativeTo(null);
    }

    private JPanel createLimitsPanel() {
        limitsPanel = new JPanel();
        limitsPanel.setLayout(new BoxLayout(limitsPanel, BoxLayout.Y_AXIS));
        limitsPanel.setBorder(BorderFactory.createTitledBorder("Category Limits"));

        // Panel z przyciskiem do dodawania limitów
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addLimitButton = new JButton("Set Category Limit");
        addLimitButton.addActionListener(e -> showSetLimitDialog());
        buttonPanel.add(addLimitButton);

        // Panel z listą limitów
        JPanel limitsListPanel = new JPanel();
        limitsListPanel.setLayout(new BoxLayout(limitsListPanel, BoxLayout.Y_AXIS));

        // Inicjalizacja etykiet dla każdej kategorii
        for (Category category : Category.values()) {
            JLabel limitLabel = new JLabel(category.name() + ": Not set");
            categoryLimitLabels.put(category, limitLabel);
            limitsListPanel.add(limitLabel);
        }

        limitsPanel.add(buttonPanel);
        limitsPanel.add(new JScrollPane(limitsListPanel));

        return limitsPanel;
    }

    private void showSetLimitDialog() {
        JDialog dialog = new JDialog(this, "Set Category Limit", true);
        dialog.setLayout(new GridLayout(3, 2, 5, 5));

        JComboBox<Category> categoryBox = new JComboBox<>(Category.values());
        JTextField limitField = new JTextField(10);
        JButton saveButton = new JButton("Save");

        dialog.add(new JLabel("Category:"));
        dialog.add(categoryBox);
        dialog.add(new JLabel("Limit:"));
        dialog.add(limitField);
        dialog.add(saveButton);

        saveButton.addActionListener(e -> {
            try {
                BigDecimal limit = new BigDecimal(limitField.getText().trim());
                if (limit.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Limit must be greater than zero");
                    return;
                }

                Category category = (Category) categoryBox.getSelectedItem();
                setCategoryLimit(category, limit);
                dialog.dispose();
                updateLimitsDisplay();
            } catch (NumberFormatException ex) {
                showError("Please enter a valid amount");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void setCategoryLimit(Category category, BigDecimal limit) {
        categoryLimits.put(category, limit);
        updateLimitLabel(category);
    }

    private void updateLimitLabel(Category category) {
        JLabel label = categoryLimitLabels.get(category);
        BigDecimal limit = categoryLimits.get(category);
        BigDecimal spent = calculateCategorySpending(category);

        if (limit != null) {
            String status = String.format("%s: $%.2f / $%.2f",
                    category.name(), spent, limit);
            label.setText(status);

            // Zmiana koloru tekstu w zależności od przekroczenia limitu
            if (spent.compareTo(limit) > 0) {
                label.setForeground(Color.RED);
            } else if (spent.compareTo(limit.multiply(new BigDecimal("0.8"))) > 0) {
                label.setForeground(Color.ORANGE);
            } else {
                label.setForeground(Color.BLACK);
            }
        }
    }

    private BigDecimal calculateCategorySpending(Category category) {
        return transactionViewModel.getAllTransactions().stream()
                .filter(t -> t.getCategory() == category)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updateLimitsDisplay() {
        for (Category category : Category.values()) {
            updateLimitLabel(category);
        }
    }

    /*************************
     *  Metoda importu CSV   *
     *************************/
    private void importTransactions() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select CSV file to import");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                transactionViewModel.importTransactionsFromCsv(filePath);
                updateDisplay();  // Odśwież UI (tabela, limity, saldo, itp.)
                showInfo("Transactions imported successfully from:\n" + filePath);
            } catch (IOException ex) {
                showError("Error importing CSV: " + ex.getMessage());
            }
        }
    }

    /*************************
     *  Metoda eksportu CSV  *
     *************************/
    private void exportTransactions() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose where to save CSV");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                transactionViewModel.exportTransactionsToCsv(filePath);
                showInfo("Transactions exported successfully to:\n" + filePath);
            } catch (IOException ex) {
                showError("Error exporting CSV: " + ex.getMessage());
            }
        }
    }


    private JPanel createTopPanel() {
        // Main top panel with BorderLayout
        JPanel mainTopPanel = new JPanel(new BorderLayout(5, 5));
        mainTopPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Income panel at the top
        JPanel incomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        monthlyIncomeField = new JTextField(10);
        JButton setIncomeButton = new JButton("Set Monthly Income");
        setIncomeButton.addActionListener(e -> setMonthlyIncome());

        incomePanel.add(new JLabel("Monthly Income: $"));
        incomePanel.add(monthlyIncomeField);
        incomePanel.add(setIncomeButton);
        incomePanel.add(balanceLabel = new JLabel("Current Balance: $0.00"));

        // Transaction input panel
        JPanel transactionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Initialize components
        categoryComboBox = new JComboBox<>(Category.values());
        amountField = new JTextField(10);
        descriptionField = new JTextField(20);

        // Add components to transaction panel
        transactionPanel.add(new JLabel("Category:"));
        transactionPanel.add(categoryComboBox);
        transactionPanel.add(new JLabel("Amount: $"));
        transactionPanel.add(amountField);
        transactionPanel.add(new JLabel("Description:"));
        transactionPanel.add(descriptionField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Transaction");
        editButton = new JButton("Edit Transaction");
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");

        // Dodajemy słuchaczy zdarzeń
        addButton.addActionListener(e -> addTransaction());
        editButton.addActionListener(e -> editTransaction());
        undoButton.addActionListener(e -> undo());
        redoButton.addActionListener(e -> redo());

        // Dodajemy przyciski do panelu
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        // --------------------- DODANE - Import i Export ---------------------
        JButton importButton = new JButton("Import CSV");
        importButton.addActionListener(e -> importTransactions());  // Metoda poniżej
        JButton exportButton = new JButton("Export CSV");
        exportButton.addActionListener(e -> exportTransactions());  // Metoda poniżej

        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        // --------------------------------------------------------------------

        // Składamy wszystko w jedną całość
        mainTopPanel.add(incomePanel, BorderLayout.NORTH);
        mainTopPanel.add(transactionPanel, BorderLayout.CENTER);
        mainTopPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainTopPanel;
    }


    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Configure table
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.setAutoCreateRowSorter(true);

        // Create scroll pane with padding
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void setupTableActions() {
        transactionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && transactionTable.getSelectedRow() != -1) {
                    editTransaction();
                }
            }
        });
    }

    private void addTransaction() {
        if (!validateInputs()) {
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountField.getText().trim());
            Category category = (Category) categoryComboBox.getSelectedItem();
            String description = descriptionField.getText().trim();

            if (description.isEmpty()) {
                description = "No description";
            }

            Transaction transaction = new Transaction(
                    amount,
                    category,
                    description,
                    Transaction.TransactionType.EXPENSE
            );

            transactionViewModel.addTransaction(transaction);
            budgetViewModel.addTransaction(transaction);

            updateDisplay();
            clearInputFields();

        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
        }
    }

    private boolean validateInputs() {
        if (!budgetSet) {
            showError("Please set your monthly income first");
            return false;
        }

        String amountStr = amountField.getText().trim();
        if (amountStr.isEmpty()) {
            showError("Please enter an amount");
            return false;
        }

        try {
            BigDecimal amount = new BigDecimal(amountStr);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Amount must be greater than zero");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid amount");
            return false;
        }

        return true;
    }

    private void editTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Please select a transaction to edit");
            return;
        }

        selectedRow = transactionTable.convertRowIndexToModel(selectedRow);
        Transaction selectedTransaction = tableModel.getTransactionAt(selectedRow);

        JDialog editDialog = createEditDialog(selectedTransaction);
        editDialog.setVisible(true);
    }

    private JDialog createEditDialog(Transaction transaction) {
        JDialog dialog = new JDialog(this, "Edit Transaction", true);
        dialog.setLayout(new BorderLayout(5, 5));

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JComboBox<Category> editCategoryBox = new JComboBox<>(Category.values());
        JTextField editAmountField = new JTextField(transaction.getAmount().toString());
        JTextField editDescriptionField = new JTextField(transaction.getDescription());
        JComboBox<Transaction.TransactionType> editTypeBox = new JComboBox<>(Transaction.TransactionType.values());

        editCategoryBox.setSelectedItem(transaction.getCategory());
        editTypeBox.setSelectedItem(transaction.getType());

        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(editCategoryBox);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(editAmountField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(editDescriptionField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(editTypeBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                BigDecimal newAmount = new BigDecimal(editAmountField.getText().trim());
                if (newAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    showError("Amount must be greater than zero");
                    return;
                }

                EditTransactionCommand editCommand = new EditTransactionCommand(
                        transaction,
                        newAmount,
                        (Category) editCategoryBox.getSelectedItem(),
                        editDescriptionField.getText().trim(),
                        (Transaction.TransactionType) editTypeBox.getSelectedItem()
                );

                transactionViewModel.executeCommand(editCommand);
                updateDisplay();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                showError("Please enter a valid amount");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);

        return dialog;
    }

    private void setMonthlyIncome() {
        try {
            String incomeStr = monthlyIncomeField.getText().trim();
            if (incomeStr.isEmpty()) {
                showError("Please enter a monthly income amount");
                return;
            }

            BigDecimal income = new BigDecimal(incomeStr);
            if (income.compareTo(BigDecimal.ZERO) <= 0) {
                showError("Monthly income must be greater than zero");
                return;
            }

            budgetViewModel.setMonthlyIncome(income);
            budgetSet = true;
            toggleTransactionControls(true);
            updateDisplay();

            showInfo("Monthly income set successfully to $" + income);

        } catch (NumberFormatException ex) {
            showError("Please enter a valid amount");
        }
    }

    private void toggleTransactionControls(boolean enabled) {
        addButton.setEnabled(enabled);
        editButton.setEnabled(enabled);
        undoButton.setEnabled(enabled);
        redoButton.setEnabled(enabled);
        amountField.setEnabled(enabled);
        descriptionField.setEnabled(enabled);
        categoryComboBox.setEnabled(enabled);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Edit Menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem categoriesItem = new JMenuItem("Manage Categories");
        categoriesItem.addActionListener(e -> openCategoryManagement());
        editMenu.add(categoriesItem);

        // View Menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem reportsItem = new JMenuItem("View Reports");
        reportsItem.addActionListener(e -> openReportView());
        viewMenu.add(reportsItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }

    private void undo() {
        transactionViewModel.undo();
        updateDisplay();
    }

    private void redo() {
        transactionViewModel.redo();
        updateDisplay();
    }

    private void clearInputFields() {
        amountField.setText("");
        descriptionField.setText("");
        categoryComboBox.setSelectedIndex(0);
    }

   // @Override
    private void updateDisplay() {
        BigDecimal balance = budgetViewModel.getCurrentTotal();
        balanceLabel.setText(String.format("Current Balance: $%.2f", balance));
        tableModel.updateData(transactionViewModel.getAllTransactions());
        updateLimitsDisplay(); // Dodane odświeżanie limitów
    }

    private void openCategoryManagement() {
        CategoryManagementView categoryView = new CategoryManagementView(categoryViewModel);
        categoryView.setVisible(true);
    }

    private void openReportView() {
        ReportService reportService = new ReportService();
        ReportView reportView = new ReportView(reportService);
        reportView.setVisible(true);
        reportView.budget=this.budgetViewModel.budget;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}