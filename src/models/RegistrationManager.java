/*
 * File: RegistrationManager.java
 * ------------------------------
 * Owner: Bisoke Group 4
 * © 2024 CMU. All rights reserved.
 */

package models;

import java.io.*;

/**
 * The RegistrationManager class handles the registration and login processes for users.
 * It utilizes external scripts to hash passwords and complete registration steps.
 */
public class RegistrationManager {
    static String hashedPassword = "";

    /**
     * Completes the registration process for a new user by hashing their password
     * and invoking an external script with user details.
     *
     * @param email the email address of the user
     * @param pass the plaintext password of the user
     * @param uuid the unique identifier of the user
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param dateOfBirth the date of birth of the user
     * @param countryISOCode the ISO code of the user's country
     * @param isHIVPositive the HIV status of the user
     * @param diagnosisDate the date the user was diagnosed with HIV
     * @param onART the ART status of the user
     * @param artStartDate the date the user started ART
     * @param role the role of the user
     * @param lifespan the estimated lifespan of the user
     */
    public void completeRegistration(String email, String pass, String uuid, String firstName, String lastName, String dateOfBirth, String countryISOCode, boolean isHIVPositive, String diagnosisDate, boolean onART, String artStartDate, String role, double lifespan) {
        // Hash the password
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/hash-password.sh", pass);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                hashedPassword = line;
            }
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Loading...");
            } else {
                System.out.println("Unable to complete registration at the moment. Please try again later or contact the system administrator.");
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }

        // Call the complete registration script
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/complete-registration.sh", email, hashedPassword, uuid, firstName, lastName, dateOfBirth, countryISOCode, String.valueOf(isHIVPositive), diagnosisDate, String.valueOf(onART), artStartDate, role, Double.toString(lifespan));
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print all output from the script
            }
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("The complete registration script was executed successfully");
            } else {
                System.out.println("An error occurred while saving your details. Please try again later.");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Logs in a user by hashing their password and invoking an external script with the login details.
     *
     * @param email the email address of the user
     * @param password the plaintext password of the user
     */
    public void userLogin(String email, String password) {
        // Hash the password
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/hash-password.sh", password);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                hashedPassword = line;
            }
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Loading...");
            } else {
                System.out.println("Unable to complete registration at the moment. Please try again later or contact the system administrator.");
            }
        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }

        // Call the login script
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/login.sh", email, hashedPassword);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String script_output;
            while ((script_output = reader.readLine()) != null) {
                System.out.println(script_output);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
