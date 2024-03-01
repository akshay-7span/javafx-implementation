# JavaFX Calculator Application
- This JavaFX calculator application allows users to perform basic arithmetic calculations and provides a scientific mode for advanced mathematical operations.

## Features
- Basic arithmetic operations: Addition, subtraction, multiplication, and division.
- Scientific mode: Provides additional mathematical functions such as sine, cosine, tangent, logarithm, square root, etc.
- Calculation history: Displays the history of calculations performed by the user.
- Database integration: Saves calculation history to a MySQL database.
## Dependencies
- JavaFX: JavaFX is used to build the graphical user interface (GUI) for the calculator application.
- MySQL Connector: The MySQL Connector for Java is required for database connectivity and storing calculation history.
## Installation and Usage
- Clone the Repository: Clone the repository to your local machine using the following command:


```https://github.com/akshay-7span/javafx-demo```

- Set Up Database: Ensure you have a MySQL database set up. Update the database connection parameters in the Calculator.java file:


```
private static final String DB_URL = "jdbc:mysql://localhost:3306/database name";
private static final String DB_USER = "databse-user-name";
private static final String DB_PASSWORD = "data-base-password";
```
- Build and Run: Open the project in IntelliJ IDEA or your preferred IDE. Run the Calculator.java file to launch the calculator application.

- Usage: Use the calculator interface to perform arithmetic calculations. Switch between basic and scientific modes using the "Scientific" button.

- View Calculation History: The history of calculations performed is displayed in the application's history viewer.