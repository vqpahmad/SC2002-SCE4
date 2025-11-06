package com.internshipapp.ui;

import com.internshipapp.controllers.ApplicationManager;
import com.internshipapp.models.Application;
import com.internshipapp.models.Student;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.models.Internship;
import com.internshipapp.models.WithdrawalRequest;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class StudentMenu implements UserMenu {
    private Student student;
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;

    public StudentMenu(Student student, InternshipManager internshipManager, ApplicationManager applicationManager) {
        this.student = student;
        this.internshipManager = internshipManager;
        this.applicationManager = applicationManager;
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println(ansi().fg(BLUE).bold().a("\n=== Student Menu ===").reset());
            System.out.println("1. View Available Internships");
            System.out.println("2. Apply for Internship");
            System.out.println("3. View My Applications");
            System.out.println("4. Accept Placement");
            System.out.println("5. Request Withdrawal");
            System.out.println("6. Change Password");
            System.out.println("0. Logout");
            System.out.print(ansi().fg(YELLOW).a("Enter your choice: ").reset());
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    viewAvailableInternships();
                    break;
                case 2:
                    applyForInternship(scanner);
                    break;
                case 3:
                    viewApplicationStatus();
                    break;
                case 4:
                    acceptPlacement(scanner);
                    break;
                case 5:
                    requestWithdrawal(scanner);
                    break;
                case 6:
                    changePassword(scanner);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println(ansi().fg(RED).a("Invalid choice. Please try again.").reset());
            }
        } while (choice != 0);
    }

    private void viewAvailableInternships() {
        List<Internship> internships = internshipManager.viewAvailableInternships(student);
        if (internships.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("No available internships matching your profile at the moment.").reset());
        } else {
            System.out.println(ansi().fg(CYAN).a("--- Available Internships ---").reset());
            for (Internship internship : internships) {
                System.out.println("ID: " + internship.getInternshipID() + " | Title: " + internship.getTitle() + " | Company: " + internship.getCompanyName());
            }
        }
    }

    private void applyForInternship(Scanner scanner) {
        viewAvailableInternships();
        System.out.print(ansi().fg(YELLOW).a("Enter the ID of the internship you want to apply for (or 0 to cancel): ").reset());
        String internshipId = scanner.nextLine();

        if (internshipId.equals("0")) {
            return;
        }

        Internship internship = internshipManager.findInternshipById(internshipId);

        if (internship != null) {
            boolean alreadyApplied = student.getApplications().stream()
                    .anyMatch(app -> app.getInternship().getInternshipID().equals(internshipId));

            if (alreadyApplied) {
                System.out.println(ansi().fg(RED).a("Error: You have already applied for this internship.").reset());
                return;
            }

            long pendingCount = student.getApplications().stream()
                    .filter(app -> app.getStatus() == com.internshipapp.enums.ApplicationStatus.PENDING)
                    .count();

            if (pendingCount >= 3) {
                System.out.println(ansi().fg(RED).a("Error: You have reached the maximum of 3 pending applications.").reset());
                return;
            }

            applicationManager.createApplication(student, internship);
            System.out.println(ansi().fg(GREEN).a("Application submitted successfully!").reset());
        } else {
            System.out.println(ansi().fg(RED).a("Invalid internship ID.").reset());
        }
    }

    private void viewApplicationStatus() {
        List<Application> applications = student.getApplications();
        if (applications.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("You have not made any applications yet.").reset());
        } else {
            System.out.println(ansi().fg(CYAN).a("--- Your Applications ---").reset());
            for (Application app : applications) {
                System.out.println("App ID: " + app.getApplicationID() + " | Internship: " + app.getInternship().getTitle() + " | Status: " + app.getStatus());
            }
        }
    }

    private void acceptPlacement(Scanner scanner) {
        List<Application> offeredPlacements = student.getApplications().stream()
                .filter(app -> app.getStatus() == com.internshipapp.enums.ApplicationStatus.APPROVED)
                .collect(Collectors.toList());

        if (offeredPlacements.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("You have no pending internship offers to accept.").reset());
            return;
        }

        System.out.println(ansi().fg(CYAN).a("--- Your Internship Offers ---").reset());
        offeredPlacements.forEach(app -> System.out.println("App ID: " + app.getApplicationID() + " | Internship: " + app.getInternship().getTitle()));
        System.out.print(ansi().fg(YELLOW).a("Enter the Application ID of the placement you wish to accept: ").reset());
        String appId = scanner.nextLine();

        Application toAccept = offeredPlacements.stream().filter(app -> app.getApplicationID().equals(appId)).findFirst().orElse(null);
        if (toAccept != null) {
            applicationManager.acceptPlacement(toAccept);
        } else {
            System.out.println(ansi().fg(RED).a("Invalid Application ID.").reset());
        }
    }

    private void requestWithdrawal(Scanner scanner) {
        viewApplicationStatus();
        System.out.print(ansi().fg(YELLOW).a("Enter the Application ID you wish to withdraw from: ").reset());
        String appId = scanner.nextLine();
        Application toWithdraw = student.getApplications().stream().filter(app -> app.getApplicationID().equals(appId)).findFirst().orElse(null);

        if (toWithdraw != null) {
            WithdrawalRequest request = applicationManager.requestWithdrawal(toWithdraw);
            if (request != null) {
                System.out.println(ansi().fg(GREEN).a("Withdrawal request submitted successfully. Request ID: " + request.getRequestID()).reset());
            } else {
                System.out.println(ansi().fg(RED).a("Failed to submit withdrawal request.").reset());
            }
        } else {
            System.out.println(ansi().fg(RED).a("Invalid Application ID.").reset());
        }
    }

    private void changePassword(Scanner scanner) {
        System.out.print(ansi().fg(YELLOW).a("Enter your current password: ").reset());
        String currentPassword = scanner.nextLine();

        if (!student.login(student.getUserID(), currentPassword)){
            System.out.println(ansi().fg(RED).a("Incorrect current password.").reset());
            return;
        }

        System.out.print(ansi().fg(YELLOW).a("Enter your new password: ").reset());
        String newPassword = scanner.nextLine();
        System.out.print(ansi().fg(YELLOW).a("Confirm your new password: ").reset());
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println(ansi().fg(RED).a("New passwords do not match. Please try again.").reset());
            return;
        }

        student.setPassword(newPassword);
        System.out.println(ansi().fg(GREEN).a("Password changed successfully.").reset());
    }
}