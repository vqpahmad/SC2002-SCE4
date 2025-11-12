package com.internshipapp.models;

import java.util.List;
import java.util.ArrayList;
import com.internshipapp.enums.*;


/**
 * Represents a company representative who can create and manage internships.
 */
public class CompanyRepresentative extends User {
    private String companyName;
    private String department;
    private String position;
    private boolean isApproved;
    private List<Internship> createdInternships; // max is 5

    /**
     * Create a new company representative account.
     *
     * @param userID unique id
     * @param name display name
     * @param password account password
     * @param companyName company name
     * @param department representative department
     * @param position representative position
     */
    public CompanyRepresentative(String userID, String name, String password, String companyName, String department, String position) {
        super(userID, name, password);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.isApproved = false; // Default to not approved
        this.createdInternships = new ArrayList<>();
    }

    /**
     * View applications for an internship owned by this representative.
     *
     * @param internship internship to inspect
     * @return list of applications or empty list if not permitted
     */
    public List<Application> viewApplications(Internship internship) {
        // Implementation for viewing applications for a specific internship
        if (internship.getOwner() != this) {
            System.out.println("You do not have permission to view applications for this internship.");
            return new ArrayList<>();
        }
        return internship.getApplications();
    }

    /**
     * Approve an application (set to APPROVED).
     *
     * @param application application to approve
     */
    public void approveApplication(Application application) {
        // Implementation for approving an application
        application.setStatus(ApplicationStatus.APPROVED);
    }

    /**
     * Reject an application (set to UNSUCCESSFUL).
     *
     * @param application application to reject
     */
    public void rejectApplication(Application application) {
        // Implementation for rejecting an application
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Toggle an internship's visibility (if owned by this rep).
     *
     * @param internship internship to toggle
     * @param isVisible new visibility
     */
    public void toggleVisibility(Internship internship, boolean isVisible) {
        // Implementation for toggling the visibility of an internship
        if (internship.getOwner() != this) {
            System.out.println("You do not have permission to change visibility for this internship.");
            return;
        }
        internship.setVisible(isVisible);
    }

    // Getters and Setters for the fields can be added here

    

    /**
     * Returns the company name associated with this representative.
     *
     * @return company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Returns the representative's department.
     *
     * @return department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Returns the representative's position/title.
     *
     * @return position/title
     */
    public String getPosition() {
        return position;
    }

    /**
     * Indicates whether this representative has been approved by staff.
     *
     * @return true if approved, false otherwise
     */
    public boolean isApproved() {
        return isApproved;
    }

    /**
     * Set the approved flag for this representative.
     *
     * @param isApproved new approval state
     */
    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
    /**
     * Returns the list of internships created by this representative.
     *
     * @return created internships
     */
    public List<Internship> getCreatedInternships() {
        return createdInternships;
    }
}