package com.internshipapp.controllers;

import com.internshipapp.models.*;
import com.internshipapp.enums.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ApplicationManager {
    private List<Application> applications;
    private List<WithdrawalRequest> withdrawalRequests;

    public ApplicationManager() {
        this.applications = new ArrayList<>();
        this.withdrawalRequests = new ArrayList<>();
    }

    public Application applyForInternship(Student student, Internship internship) {
        if (student.getApplications().size() >= 3) {
            System.out.println("Error: Cannot apply for more than 3 internships.");
            return null;
        }
        for (Application existingApp : student.getApplications()) {
            if (existingApp.getInternship().getInternshipID().equals(internship.getInternshipID())) {
                System.out.println("Error: You have already applied for this internship.");
                return null;
            }
        }

        Application newApplication = new Application(student, internship, ApplicationStatus.PENDING);
        this.applications.add(newApplication);
        student.addApplication(newApplication);
        internship.addApplication(newApplication);
        return newApplication;
    }

    public void acceptPlacement(Application application) {
        if (application != null && application.getStatus() == ApplicationStatus.APPROVED) {
            application.setStatus(ApplicationStatus.ACCEPTED);
            // Logic to withdraw other pending applications
            Student student = application.getStudent();
            for (Application otherApp : student.getApplications()) {
                if (otherApp.getStatus() == ApplicationStatus.PENDING) {
                    otherApp.setStatus(ApplicationStatus.UNSUCCESSFUL);
                }
            }
            System.out.println("Placement accepted for internship: " + application.getInternship().getTitle());
        } else {
            System.out.println("Error: This placement cannot be accepted. It may not have been offered to you.");
        }
    }

    public WithdrawalRequest requestWithdrawal(Application application) {
        if (application != null) {
            WithdrawalRequest request = new WithdrawalRequest(application);
            this.withdrawalRequests.add(request);
            return request;
        }
        return null;
    }

    public void addWithdrawalRequest(WithdrawalRequest request) {
        this.withdrawalRequests.add(request);
    }

    public List<WithdrawalRequest> getPendingWithdrawalRequests() {
        return withdrawalRequests.stream()
                .filter(r -> r.getStatus() == RequestStatus.PENDING)
                .collect(Collectors.toList());
    }

    public WithdrawalRequest findRequestById(String id) {
        return withdrawalRequests.stream()
                .filter(r -> r.getRequestID().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void processWithdrawalRequest(WithdrawalRequest request, boolean approve) {
        if (approve) {
            request.setStatus(RequestStatus.APPROVED);
            // Also update the original application status
            request.getApplication().setStatus(ApplicationStatus.UNSUCCESSFUL);
            // Optional: Re-open a slot in the internship
            Internship internship = request.getApplication().getInternship();
            if (internship.getSlotsFilled() > 0) {
                internship.setSlotsFilled(internship.getSlotsFilled() - 1);
            }
        } else {
            request.setStatus(RequestStatus.REJECTED);
        }
    }
}