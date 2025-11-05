package com.internshipapp.models;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.In;

import com.internshipapp.enums.*;

public class Application {
    private String applicationID;
    private Student student;
    private Internship internship;
    private Date applicationDate;
    private ApplicationStatus status;

    public Application(Student student, Internship internship, ApplicationStatus status) {
        this.applicationID = java.util.UUID.randomUUID().toString();
        this.student = student;
        this.internship = internship;
        this.applicationDate = new Date();
        this.status = status;
    }

    public String getApplicationID() {
        return applicationID;
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

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}