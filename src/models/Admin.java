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
        ProcessBuilder processBuilder = new ProcessBuilder("./user-manager.sh", "initiateRegistration", email);
        processBuilder.directory(new File("/Users/esthercarrelle/IdeaProjects/Life Prognosis/src"));

        String command = String.format("./user-manager.sh initiate-registration %s %s", email, uuid);
        try {
            Process process = processBuilder.start();
            // Capture the process output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

