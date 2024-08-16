/*
 * File: Admin.java
 * ------------------------------
 * Owner: Bisoke Group 4
 * © 2024 CMU. All rights reserved.
 */

package models;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static models.RegistrationManager.clearScreen;

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
            clearScreen();
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
                        "/mnt/c/AMANYA/CMU-Africa/Programming Bootcamp/Life-Prognosis/src/user-store.txt");

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
                clearScreen();
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
    /**
     * Download the user analytics and add them to csv
     */
    public void downloadAnalytics(User requestingUser, String filePath) {
        if (!(requestingUser instanceof Admin)) {
            throw new SecurityException("Access denied. Only admins can download user data.");
        }

        Map<String, ArrayList<Double>> countryExpectanciesMap = new HashMap<>();
        Map<String, Integer> countryPatientCountMap = new HashMap<>();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("\"A Report Showing the Total Patients, Average, Median and Percentiles per Country\"\n");
            writer.append("Country,Patients,Average,Median,20th,40th,60th,80th,90th\n");

            try {
                File file = new File("/mnt/c/AMANYA/CMU-Africa/Programming Bootcamp/Life-Prognosis/src/user-store.txt");
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;

                while ((st = br.readLine()) != null) {
                    String[] parts = st.split(",");
                    if (parts.length >= 13 && !parts[12].isEmpty() && !parts[6].isEmpty()) {
                        String country = parts[6];
                        try {
                            double expectancy = Double.parseDouble(parts[12]);

                            // Add expectancy to the list for country
                            countryExpectanciesMap
                                    .computeIfAbsent(country, k -> new ArrayList<>())
                                    .add(expectancy);

                            // Increment patient count
                            countryPatientCountMap.merge(country, 1, Integer::sum);
                        } catch (NumberFormatException e) {
                            continue;
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            // Process each country’s data
            for (Map.Entry<String, ArrayList<Double>> entry : countryExpectanciesMap.entrySet()) {
                String country = entry.getKey();
                ArrayList<Double> expectanciesList = entry.getValue();

                // Convert ArrayList to double array
                double[] expectancies = new double[expectanciesList.size()];
                for (int i = 0; i < expectanciesList.size(); i++) {
                    expectancies[i] = expectanciesList.get(i);
                }

                // Perform calculations
                double average = calculateAverage(expectancies);
                double median = calculateMedian(expectancies);
                double percentile20 = calculatePercentile(expectancies, 20);
                double percentile40 = calculatePercentile(expectancies, 40);
                double percentile60 = calculatePercentile(expectancies, 60);
                double percentile80 = calculatePercentile(expectancies, 80);
                double percentile90 = calculatePercentile(expectancies, 90);

                // Get patient count for the country
                int patientCount = countryPatientCountMap.get(country);

                // Write results to file
                writer.append(country).append(",")
                        .append(Integer.toString(patientCount)).append(",")
                        .append(Double.toString(average)).append(",")
                        .append(Double.toString(median)).append(",")
                        .append(Double.toString(percentile20)).append(",")
                        .append(Double.toString(percentile40)).append(",")
                        .append(Double.toString(percentile60)).append(",")
                        .append(Double.toString(percentile80)).append(",")
                        .append(Double.toString(percentile90)).append("\n");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static double calculateAverage(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.length;
    }

    private static double calculateMedian(double[] values) {
        Arrays.sort(values);
        int middle = values.length / 2;
        if (values.length % 2 == 0) {
            return (values[middle - 1] + values[middle]) / 2.0;
        } else {
            return values[middle];
        }
    }

    private static double calculatePercentile(double[] values, double percentile) {
        Arrays.sort(values);
        int index = (int) Math.ceil(percentile / 100.0 * values.length);
        return values[index - 1];
    }
}
