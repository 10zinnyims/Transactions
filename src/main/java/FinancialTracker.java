import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                Transaction transaction = new Transaction(date, time, description, vendor, amount);
                transactions.add(transaction);
                System.out.println(transaction);
            }
            scanner.close();
            System.out.println("Transaction file loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found. A new file will be created.");
        } catch (Exception e) {
            System.out.println("Error while reading transaction file: " + e.getMessage());
        }
    }

    private static void addDeposit(Scanner scanner) {

        System.out.println("========== Add Deposit ==========");
        System.out.print("Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.print("Time (HH:mm:ss): ");
        LocalTime time = LocalTime.parse(scanner.nextLine());
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        Transaction deposit = new Transaction(date, time, description, vendor, amount);
        transactions.add(deposit);
        System.out.println("Deposit added successfully.");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
            writer.write(deposit.toString() + "\n");
            writer.newLine();
            writer.close();
            System.out.println("Deposit added successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while writing to file: " + e.getMessage());
        }
    }

    private static void addPayment(Scanner scanner) {
        System.out.println("========== Add Payment ==========");
        System.out.print("Date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());
        System.out.println("Time (HH:mm:ss):");
        LocalTime time = LocalTime.parse(scanner.nextLine());
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.println("Vendor: ");
        String vendor = scanner.next();
        System.out.println("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        while (amount < 0) {
            System.out.println("Amount should be a positive number.");
            System.out.println("Amount: ");
            amount = scanner.nextDouble();
            scanner.nextLine();
        }
        Transaction payment = new Transaction(date, time, vendor, description, amount);
        transactions.add(payment);
        System.out.println("Payment added successfully.");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true));
            writer.write((payment.toString()) + "\n");
            writer.newLine();
            writer.close();
            System.out.println("Payment added successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.printf("%-12s %-12s %-30s %-20s %-15s%n", "Date", "Time", "Description", "Vendor", "Amount");
        for (Transaction transaction : transactions) {
            System.out.printf("%-12s %-12s %-30s %-20s $%-15.2f%n", transaction.getDate(),
                    transaction.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                    transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
    }

    private static void displayDeposits() {
        System.out.println("Deposits:");
        System.out.printf("%-15s %-15s %-30s %-20s %-15s %n", "Date", "Time", "Description", "From", "Amount");

        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                System.out.printf("%-15s %-15s %-30s %-20s %-15.2f %n", t.getDate(), t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
    }

    private static void displayPayments() {
        System.out.format("%-15s %-15s %-30s %-20s %-15s git sta%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("--------------------------------------------------------------");
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                System.out.format("%-15s %-15s %-30s %-20s %-15s %n", t.getDate(), t.getTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")), t.getDescription(), t.getVendor(), t.getAmount());
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                    // The report should include a total of all transaction amounts for the month.
                    LocalDate now = LocalDate.now();
                    LocalDate startDate = now.withDayOfMonth(1);
                    LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
                    filterTransactionsByDate(startDate, endDate);
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                    // The report should include a total of all transaction amounts for the month.
                    now = LocalDate.now();
                    LocalDate previousMonthStart = now.minusMonths(1).withDayOfMonth(1);
                    LocalDate previousMonthEnd = previousMonthStart.withDayOfMonth(previousMonthStart.lengthOfMonth());
                    filterTransactionsByDate(previousMonthStart, previousMonthEnd);
                    break;
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.
                    // The report should include a total of all transaction amounts for the year.
                    now = LocalDate.now();
                    LocalDate yearStartDate = now.withDayOfYear(1);
                    endDate = now.withDayOfYear(now.lengthOfYear());
                    filterTransactionsByDate(yearStartDate, endDate);
                    break;

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                    // The report should include a total of all transaction amounts for the year.
                    now = LocalDate.now();
                    LocalDate previousYearStartDate = now.minusYears(1).withDayOfYear(1);
                    LocalDate previousYearEndDate = previousYearStartDate.withDayOfYear(previousYearStartDate.lengthOfYear());
                    filterTransactionsByDate(previousYearStartDate, previousYearEndDate);
                    break;
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                    // The report should include a total of all transaction amounts for the vendor.
                    System.out.println("Enter vendor name:");
                    String vendor = scanner.nextLine().trim();
                    filterTransactionsByVendor(vendor);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
    }
}