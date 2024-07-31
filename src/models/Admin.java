package models;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Date;

public class Admin extends User {
    public Admin(String firstName, String lastName, String email, String passwordHash) {
        super(firstName, lastName, email, passwordHash);
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }

    @Override
    public void viewProfile() {
        // Admin-specific profile view
    }

    @Override
    public void updateProfile() {
        // Admin-specific profile update
    }
    public void initiatePatientRegistration(String email) {
        // Generate UUID for patient
        String uuid = java.util.UUID.randomUUID().toString();
        // Write to user-store.txt

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("./user-manager.sh", email);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Hashed Password: " + line);
            } else {
                System.out.println("An error occurred during hashing.");
            }
           while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

