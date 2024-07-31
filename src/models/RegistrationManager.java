package models;

import java.io.*;
import java.text.SimpleDateFormat;

public class RegistrationManager {
    private static final String USER_STORE = "user-store.txt";
    static String hashedPassword = "";

    public void completeRegistration(String email, String pass, String uuid, String firstName, String lastName, String dateOfBirth, String countryISOCode, boolean isHIVPositive, String diagnosisDate, boolean onART, String artStartDate, String role, double lifespan) {

        // We first hash the password
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("./hash-password.sh", pass);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                hashedPassword = line;
            }

            process.waitFor();

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Hashing successful");
            } else {
                System.out.println("An error occurred during hashing.");
            }
        }
        catch(Exception ex){
            System.out.print(ex.getMessage());
        }

        // We call the script and pass the variables
        /// lifespan
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("./complete-registration.sh", email, hashedPassword, uuid,firstName,lastName, dateOfBirth,countryISOCode, String.valueOf(isHIVPositive),diagnosisDate,String.valueOf(onART),artStartDate,role, Double.toString(lifespan));
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
                System.out.println("An error occurred while writing to the file.");
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void userLogin(String email, String password){
        // We first hash the password
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("./hash-password.sh", password);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                hashedPassword = line;
            }

            process.waitFor();

            int exitCode = process.exitValue();
            if (exitCode == 0) {
                System.out.println("Hashing successful");
            } else {
                System.out.println("An error occurred during hashing.");
            }
        }
        catch(Exception ex){
            System.out.print(ex.getMessage());
        }

        try{
            ProcessBuilder processBuilder = new ProcessBuilder("./login.sh", email, hashedPassword);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String script_output;

            while ((script_output = reader.readLine()) != null) {
                System.out.println(script_output);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
