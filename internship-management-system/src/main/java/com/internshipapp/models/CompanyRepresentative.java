package com.internshipapp.models;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.internshipapp.enums.*;
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

    public Internship createInternship(String title, String description, InternshipLevel level, String preferedMajor,
            Date openingDate, Date closingDate, int slots) {
        if (!isApproved) {
            System.out.println("Company Representative is not approved to create internships.");
            return null;
        }
        if (createdInternships.size() >= 5) {
            System.out.println("Cannot create more than 5 internships.");
            return null;
        }
        if (slots > 10) {
            System.out.println("Cannot create internship with more than 10 slots.");
            return null;
        }

        Internship newInternship = new Internship(
            title,
            description,
            level,
            preferedMajor,
            openingDate,
            closingDate,
            InternshipStatus.PENDING,
            this.companyName,
            this,
            slots,
            0,
            true // Default to visible
        );

        createdInternships.add(newInternship);
        System.out.println("Internship '" + title + "' created successfully and is pending approval.");

        return newInternship;
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