package com.internshipapp.models;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder.In;

import com.internshipapp.enums.*;
import java.util.List;
import java.util.ArrayList;

public class Internship {
    private String internshipID;
    private String title;
    private String description;
    private InternshipLevel level;
    private String preferedMajor;
    private Date openingDate;
    private Date closingDate;
    private InternshipStatus status;
    private String companyName;
    private CompanyRepresentative owner;
    private int slots; // max of 10
    private int slotsFilled;
    private boolean isVisible;
    private List<Application> applications;

   public Internship(String title, String description, InternshipLevel level, String preferedMajor,
            Date openingDate, Date closingDate, InternshipStatus status, String companyName, CompanyRepresentative owner, int slots,
            int slotsFilled, boolean isVisible) {
        this.internshipID = java.util.UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.level = level;
        this.preferedMajor = preferedMajor;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = status;
        this.companyName = companyName;
        this.owner = owner;
        this.slots = slots;
        this.slotsFilled = slotsFilled;
        this.isVisible = isVisible;
        this.applications = new ArrayList<Application>();
    } 

    // Getters and Setters

    public String getInternshipID() {
        return internshipID;
    }

    public void setInternshipID(String internshipID) {
        this.internshipID = internshipID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getSlots() {
        return slots;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getSlotsFilled() {
        return slotsFilled;
    }

    public void setSlotsFilled(int slotsFilled) {
        this.slotsFilled = slotsFilled;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public InternshipLevel getLevel() {
        return level;
    }

    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    public InternshipStatus getStatus() {
        return status;
    }

    public void setStatus(InternshipStatus status) {
        this.status = status;
    }

    public String getPreferedMajor() {
        return preferedMajor;
    }
    
    public void setPreferedMajor(String preferedMajor) {
        this.preferedMajor = preferedMajor;
    }

    public CompanyRepresentative getOwner() {
        return owner;
    }

    public void setOwner(CompanyRepresentative owner) {
        this.owner = owner;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void addApplication(Application application) {
        this.applications.add(application);
    }
}