package com.internshipapp.models;

/**
 * Base abstract class for all user types in the system.
 * Provides common identity and authentication helpers.
 */
public abstract class User {
    /** Unique identifier for the user. */
    protected String userID;
    /** Display name for the user. */
    protected String name;
    private String password;

    /**
     * Construct a user with identifying information.
     *
     * @param userID unique user identifier
     * @param name display name
     * @param password account password
     */
    public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
    }

    /**
     * Returns the user's unique identifier.
     *
     * @return the user's id
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Returns the user's display name.
     *
     * @return the user's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's password (stored in cleartext in this simple demo).
     *
     * @return the user's password (stored in cleartext in this simple demo)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Simple credential check.
     *
     * @param id candidate id
     * @param pass candidate password
     * @return true if credentials match
     */
    public boolean login(String id, String pass) {
        if (this.userID.equals(id) && this.password.equals(pass)) {
            return true;
        }
        return false; // Placeholder return value
    }

    /**
     * Perform logout actions for the user (placeholder implementation).
     */
    public void logout() {
        // Implement logout logic here
        System.out.println(name + " has been logged out.");
    }

    /**
     * Change the user's password.
     *
     * @param newPass new password value
     */
    public void setPassword(String newPass) {
        // Implement password change logic here
        password = newPass;
    }
}