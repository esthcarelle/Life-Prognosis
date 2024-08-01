/*
 * File: UserRole.java
 * ------------------------------
 * Owner: Esther
 * © 2024 TeamFlow. All rights reserved.
 */

package models;

/**
 * The UserRole enum defines the different roles that a user can have in the system.
 *
 * <p>
 * This enum includes the following roles:
 * </p>
 * <ul>
 * <li><b>ADMIN:</b> Represents an administrative user who has higher privileges and
 * can manage other users and perform system-wide operations.</li>
 * <li><b>PATIENT:</b> Represents a patient user who interacts with the system primarily
 * for accessing and managing their own health information.</li>
 * </ul>
 */
public enum UserRole {
    ADMIN,
    PATIENT
}
