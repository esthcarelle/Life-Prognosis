/*
 * File: Patient.java
 * ------------------------------
 * Owner: Bisoke Group 4
 * © 2024 CMU. All rights reserved.
 */

package models;

import models.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static models.RegistrationManager.*;

/**
 * The Patient class extends the User class, representing a patient in the system.
 * Patients have specific attributes such as date of birth, HIV status, diagnosis date,
 * ART (antiretroviral therapy) status, ART start date, and country ISO code.
 */
public class Patient extends User {
    private Date dateOfBirth;
    private boolean isHIVPositive;
    private Date diagnosisDate;
    private boolean onART;
    private Date artStartDate;
    private String countryISOCode;

    /**
     * Constructs a new Patient with the specified details.
     *
     * @param firstName the first name of the patient
     * @param lastName the last name of the patient
     * @param email the email address of the patient
     * @param passwordHash the hashed password of the patient
     * @param dateOfBirth the date of birth of the patient
     * @param isHIVPositive the HIV status of the patient
     * @param diagnosisDate the date the patient was diagnosed with HIV
     * @param onART the ART status of the patient
     * @param artStartDate the date the patient started ART
     * @param countryISOCode the ISO code of the patient's country
     */
    public Patient(String firstName, String lastName, String email, String passwordHash, Date dateOfBirth,
                   boolean isHIVPositive, Date diagnosisDate, boolean onART, Date artStartDate, String countryISOCode) {
        super(firstName, lastName, email, passwordHash);
        this.dateOfBirth = dateOfBirth;
        this.isHIVPositive = isHIVPositive;
        this.diagnosisDate = diagnosisDate;
        this.onART = onART;
        this.artStartDate = artStartDate;
        this.countryISOCode = countryISOCode;
    }

    /**
     * Returns the role of the user, which is PATIENT for instances of this class.
     *
     * @return the role of the user
     */
    @Override
    public UserRole getRole() {
        return UserRole.PATIENT;
    }

    /**
     * Displays the profile of the patient. This method is specific to patient users.
     */
    @Override
    public void viewProfile() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/Patient_View_ProfileInfo.sh", loggedInEmail);
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
     * Updates the profile of the patient. This method is specific to patient users.
     */
    @Override
    public void updateProfile(String firstname, String lastname, String DoB, boolean HIVStatus, String DiagnosisDate, boolean ARTStatus, String ARTStart) {
        // get country expectancy and re-calculate
        double countryLifespan = getCountryExpectancy(countryCode);
        DoB = (DoB == null || DoB.isEmpty())? patientDateOfBirth: DoB;
        if(HIVStatus){
            DiagnosisDate = (DiagnosisDate == null || DiagnosisDate.isEmpty())? patientDiagnosisDate:DiagnosisDate;
        } else {
            DiagnosisDate = patientDiagnosisDate;
        }
        if(ARTStatus){
            ARTStart = (ARTStart == null || ARTStart.isEmpty())? patientArtStartDate:ARTStart;
        } else {
            ARTStart = patientArtStartDate;
        }

//        System.out.println(countryLifespan);
//        System.out.println(DoB);
//        System.out.println(hasHIV);
//        System.out.println(DiagnosisDate);
//        System.out.println(patientDiagnosisDate);
//        System.out.println(onArt);
//        System.out.println(ARTStart);

        double survivalrate = calculateSurvivalRate(countryLifespan, DoB,HIVStatus,DiagnosisDate,ARTStatus,ARTStart);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/Patient_Update_ProfileInfo.sh", loggedInEmail, firstname, lastname,DoB,String.valueOf(HIVStatus),DiagnosisDate,String.valueOf(ARTStatus),ARTStart, Double.toString(survivalrate));
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
     * *Gets the country expectancy from the CSV
     */
    protected double getCountryExpectancy(String countryISOCode){
        double countryExpectancy = 0.0;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/read-expectancy.sh", countryISOCode);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                countryExpectancy = Double.parseDouble(line);
            }
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Expectancy retrieved successfully");
            } else {
                System.out.println("Error while retrieving expectancy. Please try again later.");
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return countryExpectancy;
    }

    /**
     * *Calculates Survival Rate
     */
    protected double calculateSurvivalRate(double countryLifespan, String dateOfBirth, boolean isHIVPositive, String diagnosisDate, boolean onART, String ARTStartDate){
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

            survivalRate = (diagnosisYear - birthYear) + 5;
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


    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isHIVPositive() {
        return isHIVPositive;
    }

    public void setHIVPositive(boolean HIVPositive) {
        isHIVPositive = HIVPositive;
    }

    public Date getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(Date diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public boolean isOnART() {
        return onART;
    }

    public void setOnART(boolean onART) {
        this.onART = onART;
    }

    public Date getArtStartDate() {
        return artStartDate;
    }

    public void setArtStartDate(Date artStartDate) {
        this.artStartDate = artStartDate;
    }

    public String getCountryISOCode() {
        return countryISOCode;
    }

    public void setCountryISOCode(String countryISOCode) {
        this.countryISOCode = countryISOCode;
    }
}