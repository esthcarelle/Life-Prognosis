package models;

public abstract class User {
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;

    public User(String firstName, String lastName, String email, String passwordHash) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public abstract UserRole getRole();

    public abstract void viewProfile();

    public abstract void updateProfile();
}
