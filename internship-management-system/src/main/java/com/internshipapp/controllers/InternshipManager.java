package com.internshipapp.controllers;

import com.internshipapp.models.*;
import com.internshipapp.enums.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder.In;

public class InternshipManager {

    private List<Internship> internships;

    public InternshipManager(List<Internship> internships) {
        this.internships = internships;
    }

    public InternshipManager() {
        this.internships = new java.util.ArrayList<Internship>();
    }

    public Internship createInternship(String title, String description, InternshipLevel level, String preferedMajor,
                                       Date openingDate, Date closingDate, String companyName, CompanyRepresentative owner, int slots) {
        // The Internship constructor will set the initial status to PENDING_APPROVAL
        Internship internship = new Internship(
                title,
                description,
                level,
                preferedMajor,
                openingDate,
                closingDate,
                InternshipStatus.PENDING,
                companyName,
                owner,
                slots,
                0,
                false // Initially not visible
        );
        internships.add(internship);
        return internship;
    }

    public List<Internship> viewAvailableInternships(Student student) {
        // Students should only see internships that are open and visible
        return internships.stream()
                .filter(i -> i.getStatus() == InternshipStatus.APPROVED && i.isVisible())
                .filter(i -> {
                    // Filter 1: Major must match the student's major.
                    boolean majorMatch = i.getPreferedMajor().equalsIgnoreCase(student.getMajor());

                    // Filter 2: Level must match the student's year of study.
                    boolean levelMatch;
                    if (student.getYearOfStudy() == 1) {
                        // Year 1 students can only see BASIC level internships.
                        levelMatch = (i.getLevel() == InternshipLevel.BASIC);
                    } else {
                        // Students in other years can see all levels.
                        levelMatch = true;
                    }
                    
                    return majorMatch && levelMatch;
                })
                .collect(Collectors.toList());
    }

    public List<Internship> viewAllInternships() {
        // For staff to see all internships, including pending ones
        return internships;
    }

    public void approveInternship(Internship internship) {
        internship.setStatus(InternshipStatus.APPROVED);
        internship.setVisible(true);
    }

    public void rejectInternship(Internship internship) {
        // Or you might want to remove it from the list entirely
        internship.setStatus(InternshipStatus.REJECTED);
        internship.setVisible(false);
    }

    public List<Application> viewApplicationsForInternship(Internship internship) {
        return internship.getApplications();
    }

    public void approveApplication(Application application) {
        Internship internship = application.getInternship();
        if (internship.getSlotsFilled() < internship.getSlots()) {
            application.setStatus(ApplicationStatus.APPROVED);
            internship.setSlotsFilled(internship.getSlotsFilled() + 1);
            if (internship.getSlotsFilled() == internship.getSlots()) {
                internship.setStatus(InternshipStatus.FILLED);
            }
        } else {
            System.out.println("Cannot approve application, all slots are filled.");
        }
    }

    public void rejectApplication(Application application) {
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);
    }

    public void toggleInternshipVisibility(Internship internship) {
        // Only allow toggling if the internship is approved and not yet filled
        if (internship.getStatus() == InternshipStatus.APPROVED) {
            internship.setVisible(!internship.isVisible());
            System.out.println("Visibility for internship " + internship.getInternshipID() + " set to " + internship.isVisible());
        } else {
            System.out.println("Cannot toggle visibility. Internship is not in an OPEN state.");
        }
    }

    public Internship findInternshipById(String id) {
        return internships.stream()
                .filter(i -> i.getInternshipID().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Internship> getPendingInternships() {
        return internships.stream()
                .filter(i -> i.getStatus() == InternshipStatus.PENDING)
                .collect(Collectors.toList());
    }
}