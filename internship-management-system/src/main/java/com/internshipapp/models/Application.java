package com.internshipapp.models;

import java.util.Date;

import com.internshipapp.enums.*;

public class Application {
    private String applicationID;
    private Student student;
    private Internship internship;
    private ApplicationStatus status;
    private Date applicationDate;

    public Application(String applicationID, Student student, Internship internship, ApplicationStatus status) {
        this.applicationID = applicationID;
        this.student = student;
        this.internship = internship;
        this.applicationDate = new Date();
        this.status = status;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public Student getStudent() {
        return student;
    }

    public Internship getInternship() {
        return internship;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}