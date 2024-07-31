import models.Admin;
import models.Patient;
import models.RegistrationManager;
import models.UserRole;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
// Initialize the initial admin
        Admin admin = new Admin("AdminFirstName", "AdminLastName", "admin@example.com", "adminPassHash");
        Patient patient = new Patient("", "", "", "", null, false, null, false, null, "");
        RegistrationManager regMgr = new RegistrationManager();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Admin: Initiate Patient Registration");
            System.out.println("3. Patient: Complete Registration");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Your Email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Enter Your Password: ");
                    String loginPassword = scanner.nextLine();
                    regMgr.userLogin(loginEmail,loginPassword);
                    break;
                case 2:
                    System.out.print("Enter Patient Email: ");
                    String patientEmail = scanner.nextLine();
                    admin.initiatePatientRegistration(patientEmail);
                    break;
                case 3:
                    System.out.print("Enter your password: ");
                    String pass1 = scanner.nextLine();
                    System.out.print("Re enter the password: ");
                    String pass2 = scanner.nextLine();

                    //check if the passwords match
                    if(pass1.equals(pass2)){
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();

                        System.out.print("Enter your UUID: ");
                        String uuid = scanner.nextLine();

                        System.out.print("Enter first name: ");
                        String firstName = scanner.nextLine();

                        System.out.print("Enter last name: ");
                        String lastName = scanner.nextLine();

                        System.out.print("Enter date of birth (dd-MM-yyyy): ");
                        String dOB = scanner.nextLine();

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

                            System.out.print("Are you on ART (true/false): ");
                            onART = Boolean.parseBoolean(scanner.nextLine());

                            if (onART) {
                                System.out.print("Enter ART start date (dd-MM-yyyy): ");
                                artStartDate = scanner.nextLine();
                            }
                        }

                        String role = String.valueOf(UserRole.PATIENT);

                        System.out.print("Enter lifespan (e.g., 62.5): ");
                        double lifespan = scanner.nextDouble();

                        regMgr.completeRegistration(email,pass1,uuid,firstName,lastName,dOB,countryIsoCode,hasHIV,diagnosisDate,onART,artStartDate,role,lifespan);
                    }
                    else{
                        System.out.println("Please enter matching passwords.");
                    }
                    break;
                case 4:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}