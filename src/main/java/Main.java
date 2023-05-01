import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String fileName = "transactions.csv";
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("HOME SCREEN");
            System.out.println("What would you like to do?");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("R) Reports");
            System.out.println("X) Exit");

            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "D":
                    System.out.println("Enter the deposit details following the format:");
                    System.out.println("date(yyyy-mm-dd)|time(HH:MM:SS)|description|amount");
                    String depositInput = scanner.nextLine();
                    String[] depositParts = depositInput.split("\\|"); //Think I might not split this.
                    LocalDate depositDate = LocalDate.parse(depositParts[0]);
                    LocalTime depositTime = LocalTime.parse(depositParts[1]);
                    String depositDescription = depositParts[2];
                    double depositAmount = Double.parseDouble(depositParts[3]);

                case "P":
                    System.out.println("Enter the payment details:");
                    // payment details
                case "L":
                    // Home menu, display all entries/ sub menu.
                    boolean backToHome = false;
                    while (!backToHome) {
                        System.out.println("=== LEDGER ===");
                        System.out.println("What would you like to do?");
                        System.out.println("A) All");
                        System.out.println("D) Deposits");
                        System.out.println("P) Payments");
                        System.out.println("R) Reports");
                        System.out.println("H) Home");

                    }
            }
        }
    }
}
