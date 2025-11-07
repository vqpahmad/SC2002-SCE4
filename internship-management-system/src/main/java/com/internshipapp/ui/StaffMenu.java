package com.internshipapp.ui;

import com.internshipapp.controllers.ApplicationManager;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.controllers.UserManager;
import com.internshipapp.models.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class StaffMenu implements UserMenu {
    private CareerCenterStaff staff;
    private UserManager userManager;
    private InternshipManager internshipManager;
    private ApplicationManager applicationManager;

    public StaffMenu(CareerCenterStaff staff, UserManager userManager, InternshipManager internshipManager, ApplicationManager applicationManager) {
        this.staff = staff;
        this.userManager = userManager;
        this.internshipManager = internshipManager;
        this.applicationManager = applicationManager;
    }

    @Override
    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println(ansi().fg(BLUE).bold().a("\n=== Staff Menu ===").reset());
            System.out.println("1. Authorize Company Representative");
            System.out.println("2. Approve/Reject Internship");
            System.out.println("3. Process Withdrawal Request");
            System.out.println("4. Generate Report");
            System.out.println("5. Change Password");
            System.out.println("0. Logout");
            System.out.print(ansi().fg(YELLOW).a("Enter your choice: ").reset());
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1: processRepresentative(scanner); break;
                case 2: processInternship(scanner); break;
                case 3: processWithdrawal(scanner); break;
                case 4: generateReport(scanner); break;
                case 5: changePassword(scanner); break;
                case 0: System.out.println("Logging out..."); break;
                default: System.out.println(ansi().fg(RED).a("Invalid choice. Please try again.").reset());
            }
        } while (choice != 0);
    }

    private void generateReport(Scanner scanner) {
        System.out.println(ansi().fg(CYAN).a("\n--- Generate Internship Report ---").reset());
        System.out.println("Enter filter criteria. Press Enter to skip a filter.");

        System.out.print(ansi().fg(YELLOW).a("Filter by Status (PENDING, APPROVED, REJECTED, FILLED): ").reset());
        String statusFilter = scanner.nextLine().toUpperCase();

        System.out.print(ansi().fg(YELLOW).a("Filter by Preferred Major: ").reset());
        String majorFilter = scanner.nextLine();

        System.out.print(ansi().fg(YELLOW).a("Filter by Level (BASIC, INTERMEDIATE, ADVANCED): ").reset());
        String levelFilter = scanner.nextLine().toUpperCase();

        List<Internship> reportData = internshipManager.generateReport(statusFilter, majorFilter, levelFilter);

        System.out.println(ansi().fg(CYAN).a("\n--- Report Results ---").reset());
        if (reportData.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("No internships found matching your criteria.").reset());
        } else {
            reportData.forEach(i -> System.out.println("ID: " + i.getInternshipID() + " | Title: " + i.getTitle() + " | Status: " + i.getStatus() + " | Major: " + i.getPreferedMajor()));
            System.out.println(ansi().fg(YELLOW).a("\n" + reportData.size() + " records found.").reset());
        }
    }

    private void processRepresentative(Scanner scanner) {
        List<CompanyRepresentative> pendingReps = userManager.getPendingRepresentatives();
        if (pendingReps.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("No pending company representatives to authorize.").reset());
            return;
        }
        System.out.println(ansi().fg(CYAN).a("--- Pending Representatives ---").reset());
        pendingReps.forEach(r -> System.out.println("ID: " + r.getUserID() + ", Name: " + r.getName() + ", Company: " + r.getCompanyName()));
        System.out.print(ansi().fg(YELLOW).a("Enter Representative ID to process: ").reset());
        String repId = scanner.nextLine();
        CompanyRepresentative rep = userManager.findRepresentativeById(repId);

        if (rep != null) {
            System.out.print(ansi().fg(YELLOW).a("Approve (A) or Reject (R)? ").reset());
            String action = scanner.nextLine().toUpperCase();
            if (action.equals("A")) {
                userManager.authorizeRepresentative(rep, true);
                System.out.println(ansi().fg(GREEN).a("Representative authorized.").reset());
            } else if (action.equals("R")) {
                userManager.authorizeRepresentative(rep, false);
                System.out.println(ansi().fg(GREEN).a("Representative rejected.").reset());
            } else {
                System.out.println(ansi().fg(RED).a("Invalid action.").reset());
            }
        } else {
            System.out.println(ansi().fg(RED).a("Representative not found.").reset());
        }
    }

    private void processInternship(Scanner scanner) {
        List<Internship> pendingInternships = internshipManager.getPendingInternships();
        if (pendingInternships.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("No pending internships to approve.").reset());
            return;
        }
        System.out.println(ansi().fg(CYAN).a("--- Pending Internships ---").reset());
        pendingInternships.forEach(i -> System.out.println("ID: " + i.getInternshipID() + ", Title: " + i.getTitle()));
        System.out.print(ansi().fg(YELLOW).a("Enter Internship ID to process: ").reset());
        String internId = scanner.nextLine();
        Internship internship = internshipManager.findInternshipById(internId);

        if (internship != null) {
            System.out.print(ansi().fg(YELLOW).a("Approve (A) or Reject (R)? ").reset());
            String action = scanner.nextLine().toUpperCase();
            if (action.equals("A")) {
                internshipManager.approveInternship(internship);
                System.out.println(ansi().fg(GREEN).a("Internship approved and is now open.").reset());
            } else if (action.equals("R")) {
                internshipManager.rejectInternship(internship);
                System.out.println(ansi().fg(GREEN).a("Internship rejected.").reset());
            } else {
                System.out.println(ansi().fg(RED).a("Invalid action.").reset());
            }
        } else {
            System.out.println(ansi().fg(RED).a("Internship not found.").reset());
        }
    }

    private void processWithdrawal(Scanner scanner) {
        List<WithdrawalRequest> pendingRequests = applicationManager.getPendingWithdrawalRequests();
        if (pendingRequests.isEmpty()) {
            System.out.println(ansi().fg(YELLOW).a("No pending withdrawal requests.").reset());
            return;
        }
        System.out.println(ansi().fg(CYAN).a("--- Pending Withdrawal Requests ---").reset());
        pendingRequests.forEach(r -> System.out.println("ID: " + r.getRequestID() + ", Student: " + r.getApplication().getStudent().getName()));
        System.out.print(ansi().fg(YELLOW).a("Enter Request ID to process: ").reset());
        String reqId = scanner.nextLine();
        WithdrawalRequest request = applicationManager.findRequestById(reqId);

        if (request != null) {
            System.out.print(ansi().fg(YELLOW).a("Approve (A) or Reject (R)? ").reset());
            String action = scanner.nextLine().toUpperCase();
            if (action.equals("A")) {
                applicationManager.processWithdrawalRequest(request, true);
                System.out.println(ansi().fg(GREEN).a("Withdrawal request approved.").reset());
            } else if (action.equals("R")) {
                applicationManager.processWithdrawalRequest(request, false);
                System.out.println(ansi().fg(GREEN).a("Withdrawal request rejected.").reset());
            } else {
                System.out.println(ansi().fg(RED).a("Invalid action.").reset());
            }
        } else {
            System.out.println(ansi().fg(RED).a("Request not found.").reset());
        }
    }

    private void changePassword(Scanner scanner) {
        System.out.print(ansi().fg(YELLOW).a("Enter your current password: ").reset());
        String currentPassword = scanner.nextLine();

        if (!staff.login(staff.getUserID(), currentPassword)) {
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

        staff.setPassword(newPassword);
        System.out.println(ansi().fg(GREEN).a("Password changed successfully.").reset());
    }
}