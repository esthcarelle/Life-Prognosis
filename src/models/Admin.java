/*
 * File: Admin.java
 * ------------------------------
 * Owner: Bisoke Group 4
 * © 2024 CMU. All rights reserved.
 */

package models;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The Admin class extends the User class, representing an administrator in the system.
 * Admins have additional privileges and can perform administrative tasks such as
 * initiating patient registration and downloading files.
 */
public class Admin extends User {

    /**
     * Constructs a new Admin with the specified details.
     *
     * @param firstName the first name of the admin
     * @param lastName the last name of the admin
     * @param email the email address of the admin
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
    public void updateProfile() {
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
    public void downloadFiles() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/download-csv.sh", "createCSV");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
