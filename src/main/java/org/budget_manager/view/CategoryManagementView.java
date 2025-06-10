package org.budget_manager.view;

import org.budget_manager.model.Category;
import org.budget_manager.viewModel.CategoryViewModel;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class CategoryManagementView extends JFrame {
    private final CategoryViewModel categoryViewModel;
    private final JComboBox<Category> categoryComboBox;
    private final JTextField limitField;

    public CategoryManagementView(CategoryViewModel categoryViewModel) {
        this.categoryViewModel = categoryViewModel;
        this.categoryComboBox = new JComboBox<>(Category.values());
        this.limitField = new JTextField(10);
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Category Management");
        setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryComboBox);
        inputPanel.add(new JLabel("Limit:"));
        inputPanel.add(limitField);

        JButton setLimitButton = new JButton("Set Limit");
        setLimitButton.addActionListener(e -> setLimit());

        inputPanel.add(setLimitButton);
        add(inputPanel, BorderLayout.CENTER);
    }

    private void setLimit() {
        try {
            Category category = (Category) categoryComboBox.getSelectedItem();
            BigDecimal limit = new BigDecimal(limitField.getText());
            categoryViewModel.setCategoryLimit(category, limit);
            JOptionPane.showMessageDialog(this,
                    "Limit set successfully for " + category.getDisplayName());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid number",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}