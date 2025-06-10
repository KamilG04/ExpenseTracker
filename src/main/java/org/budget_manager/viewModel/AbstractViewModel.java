package org.budget_manager.viewModel;

import org.budget_manager.observer.IBudgetObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractViewModel {
    protected List<IBudgetObserver> observers;

    public AbstractViewModel() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(IBudgetObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IBudgetObserver observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (IBudgetObserver observer : observers) {
            notifyObserver(observer);
        }
    }

    protected abstract void notifyObserver(IBudgetObserver observer);
}