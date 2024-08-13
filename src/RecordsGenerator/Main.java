package RecordsGenerator;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Main {
    private static final String[] DOMAINS = {"@testmail.com", "@example.com", "@mail.com"};
    private static final String[] COUNTRIES = {"DZA", "AGO", "BEN", "BWA", "BFA", "BDI", "CMR", "CPV", "CAF", "TCD",
            "COM", "COG", "COD", "DJI", "EGY", "GNQ", "ERI", "SWZ", "ETH", "GAB",
            "GMB", "GHA", "GIN", "GNB", "CIV", "KEN", "LSO", "LBR", "LBY", "MDG",
            "MWI", "MLI", "MRT", "MUS", "MAR", "MOZ", "NAM", "NER", "NGA", "RWA",
            "STP", "SEN", "SYC", "SLE", "SOM", "ZAF", "SSD", "SDN", "TZA", "TGO",
            "TUN", "UGA", "ZMB", "ZWE"}; // African countries only
    private static final String[] FIRST_NAMES = {"Test", "John", "Jane", "Alice", "Bob", "Charlie", "David", "Eva", "Frank", "Grace"};
    private static final String[] LAST_NAMES = {"Patient", "Doe", "Smith", "Brown", "Johnson", "Davis", "Wilson", "Taylor", "Anderson", "Thomas"};
    private static final String[] ROLES = {"PATIENT"};
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static void main(String[] args){
        List<String> patients = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            patients.add(generatePatientRecord());
        }
        try (FileWriter writer = new FileWriter("../user-store.txt")) {
            for (String patient : patients) {
                writer.write(patient + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String generateEmail(String fname, String lname) {
        Random random = new Random();
        return fname.toLowerCase() + lname.toLowerCase() + random.nextInt(1000) + DOMAINS[random.nextInt(DOMAINS.length)];
    }
    private static String generatePassword() {
        return "f07245df80ee034f9519eed5d761e5979380e417366cf1119c10328c6dfe9016"; // same password for testing
    }
    private static String randomDateBetween(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end);
        LocalDate randomDate = start.plusDays(new Random().nextInt((int) days + 1));
        return randomDate.format(DATE_FORMAT);
    }
    private static String generatePatientRecord() {
        Random random = new Random();

        // Generate random first and last name
        String fname = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lname = LAST_NAMES[random.nextInt(LAST_NAMES.length)];

        // Generate email using first name, last name, and a random domain
        String email = generateEmail(fname, lname);

        // Generate password using SHA-256 hash of a random UUID
        String password = generatePassword();

        // Generate UUID
        String uuid = UUID.randomUUID().toString();

        // Generate random date of birth between 1960-01-01 and 2005-12-31
        LocalDate dateOfBirth = LocalDate.parse(randomDateBetween(LocalDate.of(1960, 1, 1), LocalDate.of(2005, 12, 31)), DATE_FORMAT);

        // Select a random country ISO code
        String country = COUNTRIES[random.nextInt(COUNTRIES.length)];

        // Determine HIV status randomly
        boolean isHIVpositive = random.nextBoolean();

        // Initialize diagnosis date and ART start date
        String diagnosisDate = "";
        String artStartDate = "";

        // If the patient is HIV positive, generate a diagnosis date that is after the date of birth
        if (isHIVpositive) {
            LocalDate startDiagnosisDate = dateOfBirth.plusDays(1); // Ensures diagnosis is after date of birth
            LocalDate endDiagnosisDate = LocalDate.of(2020, 12, 31);
            diagnosisDate = randomDateBetween(startDiagnosisDate, endDiagnosisDate);

            // Generate ART start date that is after the diagnosis date
            LocalDate diagnosis = LocalDate.parse(diagnosisDate, DATE_FORMAT);
            LocalDate startARTDate = diagnosis.plusDays(1); // Ensures ART start date is after diagnosis date
            LocalDate endARTDate = LocalDate.of(2020, 12, 31);
            artStartDate = randomDateBetween(startARTDate, endARTDate);
        }

        // Determine if the patient is on ART, only if HIV positive
        boolean isOnART = isHIVpositive && random.nextBoolean();
        artStartDate = isOnART ? artStartDate : "";

        // Select a user role (in this case, always "PATIENT")
        String userRole = ROLES[0];

        // Get the life expectancy for the selected country
        double countryExpectancy = getCountryExpectancy(country);

        // Calculate the survival rate based on the country expectancy and patient details
        double lifespan = calculateSurvivalRate(countryExpectancy, dateOfBirth.format(DATE_FORMAT), isHIVpositive, diagnosisDate, isOnART, artStartDate);

        // Format the patient record into a CSV line
        return String.join(",",
                email,
                password,
                uuid,
                fname,
                lname,
                dateOfBirth.format(DATE_FORMAT),
                country,
                Boolean.toString(isHIVpositive),
                diagnosisDate,
                Boolean.toString(isOnART),
                artStartDate,
                userRole,
                Double.toString(lifespan)
        );
    }

    public static double getCountryExpectancy(String countryISOCode){
        double countryExpectancy = 0.0;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("../scripts/read-bulk-expectancy.sh", countryISOCode);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
//                countryExpectancy = Double.parseDouble(line);
                countryExpectancy = 62.7048;
            }
            process.waitFor();
            int exitCode = process.exitValue();
            if (exitCode == 0) {

            } else {
                System.out.println("Error while retrieving expectancy. Please try again later.");
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
        return countryExpectancy;
    }
    public static double calculateSurvivalRate(double countryLifespan, String dateOfBirth, boolean isHIVPositive, String diagnosisDate, boolean onART, String ARTStartDate){
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
}
