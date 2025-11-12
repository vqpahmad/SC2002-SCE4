package com.internshipapp.models;

import java.util.Date;

import com.internshipapp.enums.*;

/**
 * Represents a student's application to a specific internship.
 * Contains references to the applying {@link Student}, the {@link Internship}
 * and the current {@link com.internshipapp.enums.ApplicationStatus}.
 */
public class Application {
    private String applicationID;
    private Student student;
    private Internship internship;
    private ApplicationStatus status;
    private Date applicationDate;

    /**
     * Construct a new Application instance.
     *
     * @param applicationID unique identifier for the application
     * @param student the student who applied
     * @param internship the internship being applied to
     * @param status initial application status
     */
    public Application(String applicationID, Student student, Internship internship, ApplicationStatus status) {
        this.applicationID = applicationID;
        this.student = student;
        this.internship = internship;
        this.applicationDate = new Date();
        this.status = status;
    }

    /**
     * Returns the unique identifier for this application.
     *
     * @return the application ID
     */
    public String getApplicationID() {
        return applicationID;
    }

    /**
     * Set the application ID.
     *
     * @param applicationID new application identifier
     */
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    /**
     * Returns the student who submitted this application.
     *
     * @return the student who submitted this application
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Returns the internship referenced by this application.
     *
     * @return the internship referenced by this application
     */
    public Internship getInternship() {
        return internship;
    }

    /**
     * Returns the date when this application was submitted.
     *
     * @return the date the application was submitted
     */
    public Date getApplicationDate() {
        return applicationDate;
    }

    /**
     * Set the stored application date (used when loading persisted data).
     *
     * @param applicationDate the application date to set
     */
    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    /**
     * Returns the current status of this application.
     *
     * @return the current application status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Update the application's status.
     *
     * @param status new application status
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}