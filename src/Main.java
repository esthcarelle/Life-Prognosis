import models.*;
import java.io.Console;
import java.util.*;

import static models.RegistrationManager.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
// Initialize the initial admin
        Admin admin = new Admin("AdminFirstName", "AdminLastName", "admin@example.com", "adminPassHash");
        //Patient patient = new Patient("", "", "", "", null, false, null, false, null, "");
        Patient patient = new Patient("Isabelle", "Laurent", "isabelle@gmail.com", "dcdsfcew23", new Date(), true, new Date(), false, new Date(), "23");
        RegistrationManager regMgr = new RegistrationManager();
        InputValidator val = new InputValidator();

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        while (true) {
            if (loggedInRole == null) {
                clearScreen();
                displayLogo();
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
                        clearScreen();
                        displayLogo();
                        System.out.println("*****************************************************************************************");
                        System.out.println("\t\t\tLOGIN | Life Prognosis Management Tool");
                        System.out.println("*****************************************************************************************");
                        System.out.println("\n\t\t\033[3mEnter * to go back to the previous menu.\033[0m\n");
                        System.out.print("Enter Your Email: ");
                        String loginEmail = scanner.nextLine();
                        if(Objects.equals(loginEmail, "*")){
                            continue;
                        } else {
                            String loginPassword = new String(console.readPassword("Enter your password: "));
                            regMgr.userLogin(loginEmail, loginPassword);
                        }
                        break;
                    case 2:
                        clearScreen();
                        displayLogo();
                        System.out.println("*****************************************************************************************");
                        System.out.println("\t\tCOMPLETE REGISTRATION | Life Prognosis Management Tool");
                        System.out.println("*****************************************************************************************");
                        System.out.print("Enter your UUID \033[3m(Enter * to go back to the previous menu)\033[0m: ");
                        String uuid = scanner.nextLine();

                        if(Objects.equals(uuid, "*")){
                            continue;
                        } else {
                            if(regMgr.checkForUUID(uuid)){
                                System.out.print("Enter email: ");
                                String email = scanner.nextLine();
                                // validate email
                                if (val.validateEmail(email)) {

                                    String pass1 = new String(console.readPassword("Enter your password: "));
                                    String pass2 = new String(console.readPassword("Re enter password: "));

                                    //check if the passwords match
                                    if (pass1.equals(pass2)) {
                                        System.out.print("Enter first name: ");
                                        String firstName = scanner.nextLine();

                                        System.out.print("Enter last name: ");
                                        String lastName = scanner.nextLine();

                                        System.out.print("Enter date of birth (dd-MM-yyyy): ");
                                        String dOB = scanner.nextLine();
                                        // validate date
                                        if (val.validateDate(dOB)) {
                                            if(val.compareDates("01-01-1904",dOB)){
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
                                                        if(val.compareDates(dOB,diagnosisDate)){
                                                            System.out.print("Are you on ART (true/false): ");
                                                            onART = Boolean.parseBoolean(scanner.nextLine());

                                                            if (onART) {
                                                                System.out.print("Enter ART start date (dd-MM-yyyy): ");
                                                                artStartDate = scanner.nextLine();
                                                            }
                                                        } else {
                                                            System.out.println("Diagnosis date can not be before date of birth. PLease try again.");
                                                        }
                                                    } else {
                                                        System.out.println("Invalid date Format. PLease try again.");
                                                    }
                                                }


                                                String role = String.valueOf(UserRole.PATIENT);

                                                double lifespan = 0.0;

                                                regMgr.completeRegistration(email, pass1, uuid, firstName, lastName, dOB, countryIsoCode, hasHIV, diagnosisDate, onART, artStartDate, role, lifespan);

                                            } else {
                                                System.out.println("Date of birth is too far. PLease try again.");
                                            }
                                        } else {
                                            System.out.println("Invalid date Format. PLease try again.");
                                        }
                                    } else {
                                        System.out.println("\nPlease enter matching passwords.\n");
                                    }
                                } else {
                                    System.out.println("\nInvalid email format. Please try again.\n");
                                }
                            } else {
                                System.out.println("UUID not found. Please contact the system administrator and try again.");
                            }
                        }
                        break;
                    case 3:
                        clearScreen();
                        displayLogo();
                        System.out.println("*****************************************************************************************");
                        System.out.println("\nThank you for choosing the Life Prognosis Management Tool. See you later!\n");
                        System.out.println("*****************************************************************************************\n\n");
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
                        clearScreen();
                        displayLogo();
                        System.out.println("*****************************************************************************************");
                        System.out.println("\t\tPATIENT PROFILE | Life Prognosis Management Tool");
                        System.out.println("*****************************************************************************************");
                        patient.viewProfile();
                        break;
                    case 2:
                        clearScreen();
                        displayLogo();
                        System.out.println("*****************************************************************************************");
                        System.out.println("\t\tEDIT PROFILE | Life Prognosis Management Tool");
                        System.out.println("*****************************************************************************************");
                        System.out.println("\nFill in the fields you want to update only. You MUST enter the HIV status and ART status. Leave others empty.\n");

                        System.out.print("Enter firstname \033[3m(Enter * to go back to the previous menu)\033[0m: ");
                        String firstname = scanner.nextLine();

                        if(Objects.equals(firstname, "*")){
                            continue;
                        } else {
                            System.out.print("Enter lastname: ");
                            String lastname = scanner.nextLine();

                            System.out.print("Enter Date of Birth (dd-MM-yyyy): ");
                            String DoB = scanner.nextLine();

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
                                    if(val.compareDates(patientDateOfBirth,diagnosisDate)){
                                        System.out.print("Are you on ART (true/false): ");
                                        onART = Boolean.parseBoolean(scanner.nextLine());

                                        if (onART) {
                                            System.out.print("Enter ART start date (dd-MM-yyyy): ");
                                            artStartDate = scanner.nextLine();
                                        }
                                    } else {
                                        System.out.println("Date of diagnosis can not be before date of birth. PLease try again.");
                                    }
                                } else {
                                    System.out.println("Invalid date Format. PLease try again.");
                                }
                            }
                            patient.updateProfile(firstname, lastname, DoB, hasHIV, diagnosisDate, onART, artStartDate);
                        }
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
                        clearScreen();
                        displayLogo();
                        System.out.println("*****************************************************************************************");
                        System.out.println("\tINITIATE PATIENT REGISTRATION | Life Prognosis Management Tool");
                        System.out.println("*****************************************************************************************");

                        System.out.print("Enter Patient Email \033[3m(Enter * to go back to the previous menu)\033[0m: ");
                        String patientEmail = scanner.nextLine();

                        if(Objects.equals(patientEmail, "*")){
                            continue;
                        } else {
                            //validate email
                            if (val.validateEmail(patientEmail)) {
                                admin.initiatePatientRegistration(patientEmail);
                            } else {
                                System.out.println("Invalid email format. Please enter valid email and try again");
                            }
                        }
                        break;
                    case 2:
                        clearScreen();
                        displayLogo();
                        System.out.println("*****************************************************************************************");
                        System.out.println("\t\tDOWNLOAD REPORTS | Life Prognosis Management Tool");
                        System.out.println("*****************************************************************************************");
                        admin.downloadFiles(admin, "users.csv");
                        admin.downloadAnalytics(admin, "analytics.csv");
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