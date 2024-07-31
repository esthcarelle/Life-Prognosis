package models;

import java.util.Date;

public class Patient extends User {
    private Date dateOfBirth;
    private boolean isHIVPositive;
    private Date diagnosisDate;
    private boolean onART;
    private Date artStartDate;
    private String countryISOCode;

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

    @Override
    public UserRole getRole() {
        return UserRole.PATIENT;
    }

    @Override
    public void viewProfile() {
        // Patient-specific profile view
    }

    @Override
    public void updateProfile() {
        // Patient-specific profile update
    }
}

