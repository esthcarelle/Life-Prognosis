/*
 * File: User.java
 * ------------------------------
 * Owner: Bisoke
 * © 2024 CMU. All rights reserved.
 */

package models;

/**
 * The User class is an abstract base class that represents a generic user in the system.
 * It stores common user attributes and defines abstract methods for subclasses to implement
 * specific user roles and functionalities.
 */
public abstract class User {
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;

    /**
     * Constructs a new User with the specified details.
     *
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param email the email address of the user
     * @param passwordHash the hashed password of the user
     */
    public User(String firstName, String lastName, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the hashed password of the user.
     *
     * @return the hashed password of the user
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Returns the role of the user. This method must be implemented by subclasses.
     *
     * @return the role of the user
     */
    public abstract UserRole getRole();

    /**
     * Displays the profile of the user. This method must be implemented by subclasses.
     */
    public abstract void viewProfile();

    /**
     * Updates the profile of the user. This method must be implemented by subclasses.
     */
    public abstract void updateProfile(String firstname, String lastname, String DoB, String HIVStatus, String DiagnosisDate, String ARTStatus, String ARTStart);
}
