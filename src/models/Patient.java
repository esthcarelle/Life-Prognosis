/*
 * File: Patient.java
 * ------------------------------
 * Owner: Bisoke Group 4
 * © 2024 CMU. All rights reserved.
 */

package models;

import models.*;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.util.UidGenerator;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

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
    private double survivalrate;

    /**
     * Constructs a new Patient with the specified details.
     *
     * @param firstName      the first name of the patient
     * @param lastName       the last name of the patient
     * @param email          the email address of the patient
     * @param passwordHash   the hashed password of the patient
     * @param dateOfBirth    the date of birth of the patient
     * @param isHIVPositive  the HIV status of the patient
     * @param diagnosisDate  the date the patient was diagnosed with HIV
     * @param onART          the ART status of the patient
     * @param artStartDate   the date the patient started ART
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
        DoB = (DoB == null || DoB.isEmpty()) ? patientDateOfBirth : DoB;
        if (HIVStatus) {
            DiagnosisDate = (DiagnosisDate == null || DiagnosisDate.isEmpty()) ? patientDiagnosisDate : DiagnosisDate;
        } else {
            DiagnosisDate = patientDiagnosisDate;
        }
        if (ARTStatus) {
            ARTStart = (ARTStart == null || ARTStart.isEmpty()) ? patientArtStartDate : ARTStart;
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

        survivalrate = calculateSurvivalRate(countryLifespan, DoB, HIVStatus, DiagnosisDate, ARTStatus, ARTStart);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("scripts/Patient_Update_ProfileInfo.sh", loggedInEmail, firstname, lastname, DoB, String.valueOf(HIVStatus), DiagnosisDate, String.valueOf(ARTStatus), ARTStart, Double.toString(survivalrate));
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
    protected double getCountryExpectancy(String countryISOCode) {
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
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return countryExpectancy;
    }

    /**
     * *Calculates Survival Rate
     */
    protected double calculateSurvivalRate(double countryLifespan, String dateOfBirth, boolean isHIVPositive, String diagnosisDate, boolean onART, String ARTStartDate) {
        double age, survivalRate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);

        int birthYear = birthDate.getYear();
        int currentYear = LocalDate.now().getYear();

        // Calculate the age
        age = currentYear - birthYear;
        survivalRate = age;

        if (!isHIVPositive) {
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


    public void createICalendarFile(String filePath) {
        try {
            int survivalRateInInt = 0;

            if (survivalrate != 0.0)
                survivalRateInInt = (int) survivalrate;
            else
                survivalRateInInt = 360000000;

            // Create a new calendar
            Calendar calendar = new Calendar();
            calendar.add(new ProdId("-//Bisoke Group 4//iCal4j 1.0//EN"));
            calendar.add(new ProdId(Version.VALUE_2_0));

            // Set timezone
            TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
            net.fortuna.ical4j.model.TimeZone timezone = registry.getTimeZone("Africa/Kigali");
            java.util.Calendar cal = new GregorianCalendar(timezone);
            // Set event details

            cal.add(java.util.Calendar.YEAR, survivalRateInInt);

            cal.set(java.util.Calendar.MONTH, java.util.Calendar.SEPTEMBER);
            cal.set(java.util.Calendar.DAY_OF_MONTH, 15);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 10); // Set hour
            cal.set(java.util.Calendar.MINUTE, 0); // Set minute

            DateTime start = new DateTime(cal.getTime());
            start.setTimeZone(timezone);

            VEvent checkupEvent = new VEvent(start.toInstant(), new DateTime(start.getTime() + survivalRateInInt).toInstant(), "Death Day");
            checkupEvent.add(new TzId(timezone.getID()));

            // Set a unique identifier for the event
            checkupEvent.add(new Uid(UUID.randomUUID().toString()));

            // Add event to calendar
            calendar.add(checkupEvent);

            // Validate and save calendar to file
            calendar.validate();
            try (FileOutputStream out = new FileOutputStream(filePath)) {
                CalendarOutputter output = new CalendarOutputter();
                output.output(calendar, out);
            }
            System.out.println("Calendar successfully downloaded in iCalendar.ics");
        } catch (Exception e) {
            System.out.println("Error creating iCalendar file: " + e.getMessage());
        }
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