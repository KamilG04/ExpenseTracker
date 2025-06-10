# Budget Manager

A comprehensive personal finance management application built with Java Swing, demonstrating advanced software engineering principles and design patterns. This project showcases enterprise-level architecture and clean code practices suitable for professional development environments.

## 🎯 Project Overview

Budget Manager is a desktop application designed to help users track their financial transactions, set category-based spending limits, and generate detailed financial reports. The application emphasizes maintainable code architecture through the implementation of multiple design patterns and SOLID principles.

## ✨ Key Features

- **Transaction Management**: Add, edit, and remove financial transactions with full undo/redo functionality
- **Category-Based Budgeting**: Set spending limits for different expense categories with visual alerts
- **Monthly Income Tracking**: Configure monthly income with automatic balance calculations
- **Advanced Reporting**: Generate simple and detailed financial reports with categorized summaries
- **CSV Import/Export**: Seamlessly import and export transaction data for external analysis
- **Real-Time Notifications**: Dynamic alerts when approaching or exceeding budget limits
- **Professional UI**: Clean, intuitive interface built with Java Swing

## 🏗️ Architecture & Design Patterns

This project demonstrates mastery of enterprise software design patterns:

### **1. Builder Pattern**
- **Location**: `org.budget_manager.Builder` package
- **Implementation**: `ReportBuilder`, `ReportBuilderImpl`, `Director`
- **Purpose**: Constructs complex Report objects with different configurations (Simple vs Detailed reports)
- **Benefits**: Separates object construction from representation, enabling flexible report generation

### **2. Command Pattern**
- **Location**: `org.budget_manager.command` package
- **Implementation**: `ICommand`, `AddTransactionCommand`, `EditTransactionCommand`, `RemoveTransactionCommand`
- **Purpose**: Encapsulates transaction operations as objects, enabling undo/redo functionality
- **Benefits**: Provides operation history, supports macro commands, and decouples invoker from receiver

### **3. Observer Pattern**
- **Location**: `org.budget_manager.observer` package
- **Implementation**: `IBudgetObserver`, `BudgetObserver`, `EmailNotifier`, `BudgetAlertObserver`
- **Purpose**: Notifies multiple components when budget states change
- **Benefits**: Loose coupling between budget model and UI components, extensible notification system

### **4. State Pattern**
- **Location**: `org.budget_manager.state` package
- **Implementation**: `IBudgetState`, `NormalBudgetState`, `WarningBudgetState`, `ExceededBudgetState`
- **Purpose**: Manages budget status transitions based on spending thresholds
- **Benefits**: Clean state management, easy addition of new budget states

### **5. Strategy Pattern**
- **Location**: `org.budget_manager.strategy` package
- **Implementation**: `IForecastStrategy`, `LinearForecastStrategy`, `HistoricalForecastStrategy`
- **Purpose**: Provides interchangeable algorithms for expense forecasting
- **Benefits**: Algorithm flexibility, easy testing, and runtime strategy switching

### **6. Facade Pattern**
- **Location**: `org.budget_manager.Facade` package
- **Implementation**: `CsvFacade`, `CsvReader`, `CsvWriter`
- **Purpose**: Simplifies CSV file operations with a unified interface
- **Benefits**: Hides complexity of file I/O operations, provides clean API

### **7. Model-View-ViewModel (MVVM)**
- **Location**: `org.budget_manager.viewModel` package
- **Implementation**: `BudgetViewModel`, `TransactionViewModel`, `CategoryViewModel`
- **Purpose**: Separates business logic from UI components
- **Benefits**: Improved testability, clear separation of concerns, reactive data binding

## 🛠️ Technical Stack

- **Language**: Java 17+
- **UI Framework**: Java Swing
- **Architecture**: MVVM with Design Patterns
- **Build Tool**: Maven
- **Data Persistence**: CSV files
- **Development**: IntelliJ IDEA (with GUI Designer)

## 📂 Project Structure

```
src/main/java/org/budget_manager/
├── Builder/           # Builder pattern implementation
├── command/           # Command pattern for undo/redo
├── Facade/           # Facade pattern for CSV operations
├── model/            # Domain models (Transaction, Budget, etc.)
├── observer/         # Observer pattern for notifications
├── service/          # Business logic services
├── state/            # State pattern for budget management
├── strategy/         # Strategy pattern for forecasting
├── view/             # Swing UI components
├── viewModel/        # MVVM view models
└── Main.java         # Application entry point
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- IntelliJ IDEA (recommended)

### Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/budget-manager.git
cd budget-manager
```

2. Build the project:
```bash
mvn clean compile
```

3. Run the application:
```bash
mvn exec:java -Dexec.mainClass="org.budget_manager.Main"
```

## 💼 Professional Development Practices

This project demonstrates industry-standard development practices:

- **SOLID Principles**: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion
- **Clean Code**: Meaningful naming, small methods, clear abstractions
- **Design Patterns**: Multiple GoF patterns implemented correctly
- **Separation of Concerns**: Clear architectural layers
- **Extensibility**: Easy to add new features without modifying existing code
- **Maintainability**: Well-structured codebase with clear dependencies

## 🔧 Key Components

### Transaction Management
- Full CRUD operations with command pattern implementation
- Type-safe enums for categories and transaction types
- BigDecimal for precise financial calculations

### Budget Monitoring
- Real-time budget tracking with state transitions
- Category-specific spending limits
- Visual indicators for budget status

### Reporting System
- Builder pattern for flexible report generation
- Multiple report formats (Simple/Detailed)
- Comprehensive financial summaries

### Data Persistence
- CSV import/export functionality
- Facade pattern for simplified file operations
- Error handling for file I/O operations

## 📊 Future Enhancements

- Database integration (H2/PostgreSQL)
- RESTful API development
- Advanced analytics and charting
- Multi-currency support
- User authentication and profiles
- Cloud synchronization capabilities

## 🎓 Academic Context

This project was developed as a comprehensive demonstration of software engineering principles, achieving academic excellence while maintaining professional code quality standards. The implementation showcases deep understanding of:

- Object-Oriented Programming principles
- Design pattern applications
- Software architecture best practices
- Enterprise development methodologies

## 📝 License

This project is developed for educational and portfolio purposes, demonstrating professional software development capabilities.

---

## 👥 Contribution

**Note**: This codebase represents 90% of work which was done by me, with other contributor -Kacper Zajkowski creating the facade pattern implementation and some view model components. The majority of the architecture, design patterns, and core functionality represents my original development work, showcasing the ability to architect and implement complex software systems using industry-standard patterns and practices. The project serves as a comprehensive portfolio piece demonstrating readiness for enterprise software development roles.
