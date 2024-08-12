/*
 * File: Admin.java
 * ------------------------------
 * Owner: Bisoke Group 4
 * © 2024 CMU. All rights reserved.
 */

package models;

import java.io.*;

/**
 * The Admin class extends the User class, representing an administrator in the system.
 * Admins have additional privileges and can perform administrative tasks such as
 * initiating patient registration and downloading files.
 */
public class Admin extends User {

    /**
     * Constructs a new Admin with the specified details.
     *
     * @param firstName    the first name of the admin
     * @param lastName     the last name of the admin
     * @param email        the email address of the admin
     * @param passwordHash the hashed password of the admin
     */
    public Admin(String firstName, String lastName, String email, String passwordHash) {
        super(firstName, lastName, email, passwordHash);
    }

    /**
     * Returns the role of the user, which is ADMIN for instances of this class.
     *
     * @return the role of the user
     */
    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }

    /**
     * Displays the profile of the admin. This method is specific to admin users.
     */
    @Override
    public void viewProfile() {
        // Admin-specific profile view
    }

    /**
     * Updates the profile of the admin. This method is specific to admin users.
     */
    @Override
    public void updateProfile(String firstname, String lastname, String DoB, boolean HIVStatus, String DiagnosisDate, boolean ARTStatus, String ARTStart) {
        // Admin-specific profile update
    }

    /**
     * Initiates the registration process for a new patient by executing an external script.
     * The script is expected to handle the actual registration logic and return the result.
     *
     * @param email the email address of the patient to be registered
     */
    public void initiatePatientRegistration(String email) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/user-manager.sh", "initiate_registration", email);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Success");
            } else {
                System.out.println("An error occurred during hashing.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Initiates the download of files by executing an external script.
     * The script is expected to handle the actual file download logic and return the result.
     */
    public void downloadFiles(User requestingUser, String filePath) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/download-csv.sh", "createCSV");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();

            downloadUserData(requestingUser, filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void downloadUserData(User requestingUser, String filePath) {
        if (!(requestingUser instanceof Admin)) {
            throw new SecurityException("Access denied. Only admins can download user data.");
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            try {
                // File path is passed as parameter
                File file = new File(
                        "/mnt/c/AMANYA/CMU-Africa/Programming Bootcamp/tests/Life-Prognosis/src/user-store.txt");

                // Creating an object of BufferedReader class
                BufferedReader br
                        = new BufferedReader(new FileReader(file));

                // Declaring a string variable
                String st;
                // Condition holds true till
                // there is character in a string
                writer.append("Id,First Name,Last Name,Password,Email,Date of Birth,Has HIV,Diagnosis Date,On ART Drugs,ART Start Date,Country of Residence,Role\n");

                while ((st = br.readLine()) != null) {

                    String[] parts = st.split(",");

                    // Assuming that the fields match the order in the User class
                    String email = parts[0];
                    String passwordHash = parts[1];
                    String id = parts[2];
                    String firstName = parts[3];
                    String lastName = parts[4];
                    String dob = parts[5];
                    String country = parts[6];
                    boolean isHIVPositive = Boolean.parseBoolean(parts[7]);
                    String artStartDate = parts[8];
                    boolean isOnART = Boolean.parseBoolean(parts[9]);
                    String diagnosisDate = parts[10];
                    String role = parts[11];

                    writer.append(id).append(",").
                            append(firstName).append(",")
                            .append(lastName).append(",")
                            .append(passwordHash).append(",")
                            .append(email).append(",")
                            .append(dob).append(",")
                            .append(isHIVPositive + "").append(",")
                            .append(diagnosisDate).append(",")
                            .append(isOnART + "").append(",")
                            .append(artStartDate).append(",")
                            .append(country).append(",").append(role).append("\n");
                }
                System.out.println("User info successfully downloaded!");
            } catch (IOException | NumberFormatException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);

        } catch (Exception c) {
            System.out.println(c.getMessage());
        }
    }
}
