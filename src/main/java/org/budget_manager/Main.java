package org.budget_manager;

import org.budget_manager.view.HomeView;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
        });
    }
}