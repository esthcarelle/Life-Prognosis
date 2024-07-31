import models.Admin;
import models.Patient;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static Admin admin;
    public static void main(String[] args) {
// Initialize the initial admin
        admin = new Admin("AdminFirstName", "AdminLastName", "admin@example.com", "adminPassHash");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Admin: Initiate Patient Registration");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Patient Email: ");
                    String patientEmail = scanner.nextLine();
                    admin.initiatePatientRegistration(patientEmail);
                    break;
                case 2:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}