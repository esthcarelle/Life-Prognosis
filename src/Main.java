import models.*;

import java.io.Console;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static models.RegistrationManager.loggedInEmail;
import static models.RegistrationManager.loggedInRole;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
// Initialize the initial admin
        Admin admin = new Admin("AdminFirstName", "AdminLastName", "admin@example.com", "adminPassHash");
        Patient patient = new Patient("", "", "", "", null, false, null, false, null, "");
        RegistrationManager regMgr = new RegistrationManager();
        InputValidator val = new InputValidator();

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        while (true) {
            if (loggedInRole == null) {
                System.out.println("*****************************************************************************************");
                System.out.println("\t\t\tLife Prognosis Management Tool");
                System.out.println("*****************************************************************************************");

                System.out.println("\nWelcome. Please login to use the tool.");
                System.out.println("1. Login");
                System.out.println("2. Complete Patient Profile");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter Your Email: ");
                        String loginEmail = scanner.nextLine();
                        String loginPassword = new String(console.readPassword("Enter your password: "));
                        regMgr.userLogin(loginEmail, loginPassword);
                        break;
                    case 2:
                        String pass1 = new String(console.readPassword("Enter your password: "));
                        String pass2 = new String(console.readPassword("Re enter password: "));

                        //check if the passwords match
                        if(pass1.equals(pass2)){
                            System.out.print("Enter email: ");
                            String email = scanner.nextLine();
                            // validate email
                            if(val.validateEmail(email)){
                                System.out.print("Enter your UUID: ");
                                String uuid = scanner.nextLine();

                                System.out.print("Enter first name: ");
                                String firstName = scanner.nextLine();

                                System.out.print("Enter last name: ");
                                String lastName = scanner.nextLine();

                                System.out.print("Enter date of birth (dd-MM-yyyy): ");
                                String dOB = scanner.nextLine();
                                // validate date
                                if(val.validateDate(dOB)){
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
                                        if(val.validateDate(diagnosisDate)){
                                            System.out.print("Are you on ART (true/false): ");
                                            onART = Boolean.parseBoolean(scanner.nextLine());

                                            if (onART) {
                                                System.out.print("Enter ART start date (dd-MM-yyyy): ");
                                                artStartDate = scanner.nextLine();
                                            }
                                        }
                                        else{
                                            System.out.println("Invalid date Format. PLease try again.");
                                        }
                                    }

                                    String role = String.valueOf(UserRole.PATIENT);

                                    double lifespan = 0.0;

                                    regMgr.completeRegistration(email,pass1,uuid,firstName,lastName,dOB,countryIsoCode,hasHIV,diagnosisDate,onART,artStartDate,role,lifespan);

                                }
                                else{
                                    System.out.println("Invalid date Format. PLease try again.");
                                }
                            }
                            else{
                                System.out.println("\nInvalid email format. Please try again.\n");
                            }

                        }
                        else{
                            System.out.println("\nPlease enter matching passwords.\n");
                        }
                        break;
                    case 3:
                        System.out.println("\n\nThank you for choosing the Life Prognosis Management Tool. See you later!\n\n");
                        scanner.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else if (loggedInEmail != null && Objects.equals(loggedInRole, "PATIENT")) {
                System.out.println("*****************************************************************************************");
                System.out.println("\t\t\tPATIENT DASHBOARD");
                System.out.println("*****************************************************************************************");

                System.out.println("\nWelcome. Please select a menu option to continue.");
                System.out.println("1. View Profile");
                System.out.println("2. Update Profile");
                System.out.println("3. Logout");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume newline

                switch (choice) {
                    case 1:
                        System.out.println("View Profile");
                        break;
                    case 2:
                        System.out.println("Update Profile");
                        break;
                    case 3:
                        regMgr.logout();
                        break;
                }

            } else {
                System.out.println("*****************************************************************************************");
                System.out.println("\t\t\tADMIN DASHBOARD");
                System.out.println("*****************************************************************************************");

                System.out.println("\nWelcome. Please select a menu option to continue.");
                System.out.println("1. Initiate Patient Registration");
                System.out.println("2. Download files");
                System.out.println("3. Logout");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter Patient Email: ");
                        String patientEmail = scanner.nextLine();
                        //validate email
                        if(val.validateEmail(patientEmail)){
                            admin.initiatePatientRegistration(patientEmail);
                        }
                        else{
                            System.out.println("Invalid email format. Please enter valid email and try again");
                        }
                        break;
                    case 2:
                        admin.downloadFiles();
                        break;
                    case 3:
                        regMgr.logout();
                        break;
                }

            }
            System.out.println("\n\n");
        }
    }
}