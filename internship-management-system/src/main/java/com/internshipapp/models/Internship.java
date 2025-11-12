package com.internshipapp.models;

import java.util.Date;

import com.internshipapp.enums.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents an internship posting created by a company representative.
 * Stores metadata such as title, description, dates, slots and owner.
 */
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

    /**
     * Construct a fully-specified Internship instance (used when loading persisted data).
     *
     * @param internshipID unique identifier
     * @param title internship title
     * @param description internship description
     * @param level internship experience level
     * @param preferedMajor preferred major for applicants
     * @param openingDate opening date
     * @param closingDate closing date
     * @param status current internship status
     * @param companyName company offering the internship
     * @param owner owner {@link com.internshipapp.models.CompanyRepresentative}
     * @param slots total available slots
     * @param slotsFilled how many slots have been filled
     * @param isVisible whether the internship is visible to students
     */
    public Internship(String internshipID, String title, String description, InternshipLevel level, String preferedMajor,
            Date openingDate, Date closingDate, InternshipStatus status, String companyName, CompanyRepresentative owner, int slots,
            int slotsFilled, boolean isVisible) {
        this.internshipID = internshipID;
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

    /**
     * Convenience constructor that creates a new Internship with a generated ID.
     *
     * @param title internship title
     * @param description internship description
     * @param level internship level
     * @param preferedMajor preferred major
     * @param openingDate opening date
     * @param closingDate closing date
     * @param status initial status
     * @param companyName company name
     * @param owner owner representative
     * @param slots total slots
     * @param slotsFilled initial slots filled
     * @param isVisible visibility flag
     */
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

    /**
     * Returns the unique identifier for this internship.
     *
     * @return the internship ID
     */
    public String getInternshipID() {
        return internshipID;
    }

    /**
     * Set the internship ID.
     *
     * @param internshipID new ID
     */
    public void setInternshipID(String internshipID) {
        this.internshipID = internshipID;
    }

    /**
     * Returns the internship title.
     *
     * @return the internship title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the internship title.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the internship description.
     *
     * @return the internship description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description text.
     *
     * @param description new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the opening date for the internship.
     *
     * @return the opening date
     */
    public Date getOpeningDate() {
        return openingDate;
    }

    /**
     * Set the opening date for this internship.
     *
     * @param openingDate the new opening date
     */
    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    /**
     * Returns the closing (end) date for the internship.
     *
     * @return the closing date
     */
    public Date getClosingDate() {
        return closingDate;
    }

    /**
     * Set the closing (end) date for this internship.
     *
     * @param closingDate the date the internship closes
     */
    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    /**
     * Returns the name of the company offering this internship.
     *
     * @return the offering company name
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set the offering company name for this internship.
     *
     * @param companyName name of the company
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Returns the total number of available slots for this internship.
     *
     * @return total number of slots for this internship
     */
    public int getSlots() {
        return slots;
    }

    /**
     * Set the total number of slots available for this internship.
     *
     * @param slots new total number of slots
     */
    public void setSlots(int slots) {
        this.slots = slots;
    }

    /**
     * Returns how many slots have been filled so far.
     *
     * @return how many slots have been filled
     */
    public int getSlotsFilled() {
        return slotsFilled;
    }

    /**
     * Set how many slots have been filled so far.
     *
     * @param slotsFilled number of filled slots
     */
    public void setSlotsFilled(int slotsFilled) {
        this.slotsFilled = slotsFilled;
    }

    /**
     * Returns whether this internship is visible to students.
     *
     * @return true if this internship is visible to students
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Set the visibility of this internship for students.
     *
     * @param visible whether the internship should be visible
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * Returns the experience level required by this internship.
     *
     * @return the internship level
     */
    public InternshipLevel getLevel() {
        return level;
    }

    /**
     * Set the internship's experience level.
     *
     * @param level the new internship level
     */
    public void setLevel(InternshipLevel level) {
        this.level = level;
    }

    /**
     * Returns the current status of this internship.
     *
     * @return the current internship status
     */
    public InternshipStatus getStatus() {
        return status;
    }

    /**
     * Update the current status of the internship.
     *
     * @param status new status for the internship
     */
    public void setStatus(InternshipStatus status) {
        this.status = status;
    }

    /**
     * Returns the preferred major for applicants to this internship.
     *
     * @return preferred major for the internship
     */
    public String getPreferedMajor() {
        return preferedMajor;
    }
    
    /**
     * Set the preferred major preferred for this internship.
     *
     * @param preferedMajor preferred major to set
     */
    public void setPreferedMajor(String preferedMajor) {
        this.preferedMajor = preferedMajor;
    }

    /**
     * Returns the company representative who created/owns this internship.
     *
     * @return the company representative who owns this internship
     */
    public CompanyRepresentative getOwner() {
        return owner;
    }

    /**
     * Assign the owner (company representative) for this internship.
     *
     * @param owner the owner to assign
     */
    public void setOwner(CompanyRepresentative owner) {
        this.owner = owner;
    }

    /**
     * Returns the list of applications submitted to this internship.
     *
     * @return list of applications submitted to this internship
     */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * Add an application to the internal list.
     *
     * @param application application to add
     */
    public void addApplication(Application application) {
        this.applications.add(application);
    }
}