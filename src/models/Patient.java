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
import java.util.Date;

import static models.RegistrationManager.loggedInEmail;

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
    public void updateProfile() {
        // Patient-specific profile update
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
