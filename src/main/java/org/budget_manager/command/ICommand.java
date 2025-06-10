package org.budget_manager.command;

public interface ICommand {
    void execute();
    void undo();
    void redo();  // Added redo method
}
