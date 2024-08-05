import models.*;

import java.io.Console;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
// Initialize the initial admin
        Admin admin = new Admin("AdminFirstName", "AdminLastName", "admin@example.com", "adminPassHash");
        RegistrationManager regMgr = new RegistrationManager();
        InputValidator val = new InputValidator();

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        while (true) {
            System.out.println("*****************************************************************************************");
            System.out.println("\t\t\tLife Prognosis Management Tool");
            System.out.println("*****************************************************************************************");

            System.out.println("\nWelcome. Please select a menu option to use the tool.");
            System.out.println("1. Login");
            System.out.println("2. Admin: Initiate Patient Registration");
            System.out.println("3. Patient: Complete Registration");
            System.out.println("4. Admin: Download CSV files");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Your Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Enter Your Password: ");
                    String loginPassword = new String(console.readPassword("Enter password: "));
                    regMgr.userLogin(loginEmail, loginPassword);
                    break;
                case 2:
                    System.out.print("Enter Patient Email: ");
                    String patientEmail = scanner.nextLine();
                    //validate email
                    if (val.validateEmail(patientEmail)) {
                        admin.initiatePatientRegistration(patientEmail);
                    } else {
                        System.out.println("Invalid email format. Please enter valid email and try again");
                    }
                    break;
                case 3:
                    System.out.print("Enter your password: ");
                    //String pass1 = scanner.nextLine();
                    String pass1 = new String(console.readPassword("Enter password: "));

                    System.out.print("Re enter the password: ");
                    //String pass2 = scanner.nextLine();
                    String pass2 = new String(console.readPassword("Re enter password: "));

                    //check if the passwords match
                    if (pass1.equals(pass2)) {
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        // validate email
                        if (val.validateEmail(email)) {
                            System.out.print("Enter your UUID: ");
                            String uuid = scanner.nextLine();

                            System.out.print("Enter first name: ");
                            String firstName = scanner.nextLine();

                            System.out.print("Enter last name: ");
                            String lastName = scanner.nextLine();

                            System.out.print("Enter date of birth (dd-MM-yyyy): ");
                            String dOB = scanner.nextLine();
                            // validate date
                            if (val.validateDate(dOB)) {
                                System.out.print("Enter country ISO code: ");
                                String countryIsoCode = scanner.nextLine();

                                System.out.print("Are you HIV positive (true/false): ");
                                boolean hasHIV = Boolean.parseBoolean(scanner.nextLine());

                                String diagnosisDate = "";
                                String artStartDate = "";
                                boolean onART = false;

                                if (hasHIV) {
                                    System.out.print("Enter diagnosis date (dd-MM-yyyy): ");
                                    diagnosisDate = scanner.nextLine();
                                    //validate diagnosis date
                                    if (val.validateDate(diagnosisDate)) {
                                        System.out.print("Are you on ART (true/false): ");
                                        onART = Boolean.parseBoolean(scanner.nextLine());

                                        if (onART) {
                                            System.out.print("Enter ART start date (dd-MM-yyyy): ");
                                            artStartDate = scanner.nextLine();
                                        }
                                    } else {
                                        System.out.println("Invalid date Format. PLease try again.");
                                    }
                                }

                                String role = String.valueOf(UserRole.PATIENT);

                                double lifespan = 0.0;

                                regMgr.completeRegistration(email, pass1, uuid, firstName, lastName, dOB, countryIsoCode, hasHIV, diagnosisDate, onART, artStartDate, role, lifespan);

                            } else {
                                System.out.println("Invalid date Format. PLease try again.");
                            }
                        } else {
                            System.out.println("\nInvalid email format. Please try again.\n");
                        }

                    } else {
                        System.out.println("\nPlease enter matching passwords.\n");
                    }
                    break;
                case 4:
                    admin.downloadFiles();
                    Patient patient = new Patient("Isabelle", "Laurent", "isabelle@gmail.com", "dcdsfcew23", new Date(), true, new Date(), false, new Date(), "23");

                    Map<String, User> users = new HashMap<>();
                    users.put("key", patient);

                    admin.downloadUserData(admin, "users.csv", users);

                    break;
                case 5:
                    System.out.println("\n\nThank you for choosing the Life Prognosis Management Tool. See you later!\n\n");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\n\n");
        }
    }
}