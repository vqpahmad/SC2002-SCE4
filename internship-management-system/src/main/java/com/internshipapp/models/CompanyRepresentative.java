package com.internshipapp.models;

import java.util.List;
import java.util.ArrayList;
import com.internshipapp.enums.*;


public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private boolean isApproved;
    private List<Internship> createdInternships; // max is 5

    public CompanyRepresentative(String userID, String name, String password, String companyName, String department, String position) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.isApproved = false; // Default to not approved
        this.createdInternships = new ArrayList<>();
    }

    public List<Application> viewApplications(Internship internship) {
        // Implementation for viewing applications for a specific internship
        if (internship.getOwner() != this) {
            System.out.println("You do not have permission to view applications for this internship.");
            return new ArrayList<>();
        }
        return internship.getApplications();
    }

    public void approveApplication(Application application) {
        // Implementation for approving an application
        application.setStatus(ApplicationStatus.APPROVED);
    }

    public void rejectApplication(Application application) {
        // Implementation for rejecting an application
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    public void toggleVisibility(Internship internship, boolean isVisible) {
        // Implementation for toggling the visibility of an internship
        if (internship.getOwner() != this) {
            System.out.println("You do not have permission to change visibility for this internship.");
            return;
        }
        internship.setVisible(isVisible);
    }

    // Getters and Setters for the fields can be added here

    public String getCompanyName() {
        return companyName;
    }

    public String getDepartment() {
        return department;
    }

    public String getPosition() {
        return position;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public List<Internship> getCreatedInternships() {
        return createdInternships;
    }
}