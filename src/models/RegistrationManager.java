/*
 * File: RegistrationManager.java
 * ------------------------------
 * Owner: Bisoke Group 4
 * © 2024 CMU. All rights reserved.
 */

package models;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The RegistrationManager class handles the registration and login processes for users.
 * It utilizes external scripts to hash passwords and complete registration steps.
 */
public class RegistrationManager {
    static String hashedPassword = "";
    // login variables
    public static String loggedInEmail = null;
    public static String loggedInRole = null;
    public static String countryCode = null;
    public static boolean hasHIV = false;
    public static boolean onArt = false;
    public static String patientDiagnosisDate = null;
    public static String patientArtStartDate = null;
    public static  String patientDateOfBirth = null;

    Patient p = new Patient("", "", "", "", null, false, null, false, null, "");
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

        // get the lifespan of the country and then calculate survival rate
        double countryExpectancy = p.getCountryExpectancy(countryISOCode);
//        try {
//            ProcessBuilder processBuilder = new ProcessBuilder("scripts/read-expectancy.sh", countryISOCode);
//            Process process = processBuilder.start();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                countryExpectancy = Double.parseDouble(line);
//            }
//            process.waitFor();
//            int exitCode = process.exitValue();
//            if (exitCode == 0) {
//                System.out.println("Expectancy retrieved successfully");
//            } else {
//                System.out.println("Error while retrieving expectancy. Please try again later.");
//            }
//        } catch(Exception ex) {
//            System.out.println(ex.getMessage());
//        }
        // Calculate survival Rate
        //lifespan = calculateSurvivalRate(countryExpectancy,dateOfBirth,isHIVPositive,diagnosisDate,onART,artStartDate);
        lifespan = p.calculateSurvivalRate(countryExpectancy,dateOfBirth,isHIVPositive,diagnosisDate,onART,artStartDate);

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
     * *Calculates Survival Rate
     */
    private double calculateSurvivalRate(double countryLifespan, String dateOfBirth, boolean isHIVPositive, String diagnosisDate, boolean onART, String ARTStartDate){
        double age, survivalRate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);

        int birthYear = birthDate.getYear();
        int currentYear = LocalDate.now().getYear();

        // Calculate the age
        age = currentYear - birthYear;
        survivalRate = age;

        if(!isHIVPositive){
            survivalRate = countryLifespan - age;
        } else if (!onART) {
            LocalDate diagnoseDate = LocalDate.parse(diagnosisDate, formatter);
            int diagnosisYear = diagnoseDate.getYear();

            survivalRate = diagnosisYear + 5;
        } else {
            LocalDate diagnoseDate = LocalDate.parse(diagnosisDate, formatter);
            int diagnosisYear = diagnoseDate.getYear();

            LocalDate artStartDate = LocalDate.parse(ARTStartDate, formatter);
            int artStartYear = artStartDate.getYear();

            int noTherapyYears = artStartYear - diagnosisYear;

            survivalRate = countryLifespan - age;
            for (int i = 0; i < noTherapyYears; i++) {
                survivalRate *= 0.9;
            }
        }
        int roundedValue = (int) Math.ceil(survivalRate);
        survivalRate = (double) roundedValue;
        return survivalRate;
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
                String[] outputs = script_output.split(",");

                loggedInEmail = outputs.length > 0 ? outputs[0].trim() : null;
                loggedInRole = outputs.length > 1 ? outputs[1].trim() : null;
                countryCode = outputs.length > 2 ? outputs[2].trim() : null;
                if (outputs.length > 3 && outputs[3].trim().equals("Positive")) {
                    hasHIV = true;
                }
                onArt = outputs.length > 4 && Boolean.parseBoolean(outputs[4].trim());
                patientDiagnosisDate = outputs.length > 5 ? outputs[5].trim() : null;
                patientArtStartDate = outputs.length > 6 ? outputs[6].trim() : null;
                patientDateOfBirth = outputs.length > 7 ? outputs[7].trim() : null;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Logs out a user by clearing the email and role variables
     */
    public void logout(){
        loggedInRole = null;
        loggedInEmail = null;
        countryCode = null;
        hasHIV = false;
        onArt = false;
        patientDateOfBirth = null;
        patientDiagnosisDate = null;
        patientArtStartDate = null;
    }
}
