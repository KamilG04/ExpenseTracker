package org.budget_manager.viewModel;

import org.budget_manager.model.Category;
import org.budget_manager.observer.IBudgetObserver;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CategoryViewModel extends AbstractViewModel {
    private final Map<Category, BigDecimal> categoryLimits;

    public CategoryViewModel() {
        super();
        this.categoryLimits = new HashMap<>();
        initializeCategories();
    }

    private void initializeCategories() {
        for (Category category : Category.values()) {
            categoryLimits.put(category, BigDecimal.ZERO);
        }
    }

    public void setCategoryLimit(Category category, BigDecimal limit) {
        categoryLimits.put(category, limit);
        notifyObservers();
    }

    public Map<Category, BigDecimal> getCategoryLimits() {
        return new HashMap<>(categoryLimits);
    }

    @Override
    protected void notifyObserver(IBudgetObserver observer) {
        observer.update(null, null); // Implement logic if needed
    }
}
