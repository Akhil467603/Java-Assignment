import java.io.*;
import java.util.*;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}

class Expense {
    private String date;
    private String category;
    private double amount;

    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return date + " | " + category + " | $" + amount;
    }
}

class FileHandler {
    private static final String FILE_PATH = "expenses.txt";

    public static void saveExpenses(List<Expense> expenses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Expense expense : expenses) {
                writer.write(expense.getDate() + "," + expense.getCategory() + "," + expense.getAmount());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    public static List<Expense> loadExpenses() {
        List<Expense> expenses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String date = parts[0];
                    String category = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    expenses.add(new Expense(date, category, amount));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading expenses: " + e.getMessage());
        }
        return expenses;
    }
}

public class ExpenseTracker {
    private static Scanner scanner = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    private static User loggedInUser = null;
    private static List<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        loadTestData();
        expenses = FileHandler.loadExpenses();
        while (true) {
            if (loggedInUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void loadTestData() {
        users.add(new User("user1", "password1"));
        users.add(new User("user2", "password2"));
    }

    private static void showLoginMenu() {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                register();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.authenticate(password)) {
                loggedInUser = user;
                System.out.println("Login successful!");
                return;
            }
        }
        System.out.println("Invalid username or password");
    }

    private static void register() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        users.add(new User(username, password));
        System.out.println("Registration successful!");
    }

    private static void showMainMenu() {
        System.out.println("1. Add Expense");
        System.out.println("2. View Expenses");
        System.out.println("3. View Category-wise Summation");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                addExpense();
                break;
            case 2:
                viewExpenses();
                break;
            case 3:
                viewCategoryWiseSummation();
                break;
            case 4:
                logout();
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private static void addExpense() {
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        expenses.add(new Expense(date, category, amount));
        FileHandler.saveExpenses(expenses);
        System.out.println("Expense added successfully!");
    }

    private static void viewExpenses() {
        for (Expense expense : expenses) {
            System.out.println(expense);
        }
    }

    private static void viewCategoryWiseSummation() {
        Map<String, Double> categorySum = new HashMap<>();
        for (Expense expense : expenses) {
            categorySum.put(expense.getCategory(), categorySum.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
        }
        for (Map.Entry<String, Double> entry : categorySum.entrySet()) {
            System.out.println(entry.getKey() + ": $" + entry.getValue());
        }
    }

    private static void logout() {
        loggedInUser = null;
        System.out.println("Logged out successfully!");
    }
}
