package com.internshipapp.models;

public abstract class User {
    protected String userID;
    protected String name;
    private String password;

    public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public boolean login(String id, String pass) {
        if (this.userID.equals(id) && this.password.equals(pass)) {
            return true;
        }
        return false; // Placeholder return value
    }

    public void logout() {
        // Implement logout logic here
        System.out.println(name + "has been logged out.");
    }

    public void setPassword(String newPass) {
        // Implement password change logic here
        password = newPass;
    }
}