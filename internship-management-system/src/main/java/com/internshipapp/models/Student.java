package com.internshipapp.models;

import com.internshipapp.enums.ApplicationStatus;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private int yearOfStudy;
    private String major;
    private List<Application> applications; // max 3 applications

    public Student(String userID, String name, String password, int yearOfStudy, String major) {
        super(userID, name, password);
        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.applications = new ArrayList<>();
    }

    // addAppliction, applying would be through a controller
    public void addApplication(Application application) {
        this.applications.add(application);
    }

    /**
     * Returns a list of all applications made by the student.
     */
    public List<Application> getApplications() {
        return this.applications;
    }

    /**
     * Accepts an internship offer. This changes the application status.
     * Additional logic (like withdrawing other applications) should be handled by a controller.
     */
    public void acceptPlacement(Application application) {
        if (this.applications.contains(application) && application.getStatus() == ApplicationStatus.APPROVED) {
            application.setStatus(ApplicationStatus.ACCEPTED);
            System.out.println("Placement accepted for internship: " + application.getInternship().getInternshipID());
        } else {
            System.out.println("Cannot accept this placement. It might not have been offered or does not exist.");
        }
    }

    /**
     * Creates a request to withdraw from an internship application.
     */
    public WithdrawalRequest requestWithdrawal(Application application) {
        if (this.applications.contains(application)) {
            // The request is created, but its approval is handled by CareerCenterStaff
            return new WithdrawalRequest(application);
        }
        System.out.println("Cannot request withdrawal for an application you have not made.");
        return null;
    }

    // Getters and Setters
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}