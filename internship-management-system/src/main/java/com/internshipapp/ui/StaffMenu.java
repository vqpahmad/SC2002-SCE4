package com.internshipapp.ui;

import com.internshipapp.controllers.ApplicationManager;
import com.internshipapp.controllers.InternshipManager;
import com.internshipapp.controllers.UserManager;
import com.internshipapp.models.*;

import java.util.List;
import java.util.Scanner;

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
            System.out.println("\n=== Staff Menu ===");
            System.out.println("1. Authorize Company Representative");
            System.out.println("2. Approve/Reject Internship");
            System.out.println("3. Process Withdrawal Request");
            System.out.println("4. Generate Report");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    processRepresentative(scanner);
                    break;
                case 2:
                    processInternship(scanner);
                    break;
                case 3:
                    processWithdrawal(scanner);
                    break;
                case 4:
                    System.out.println("Report generation is not yet implemented.");
                    break;
                case 0:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void processRepresentative(Scanner scanner) {
        List<CompanyRepresentative> pendingReps = userManager.getPendingRepresentatives();
        if (pendingReps.isEmpty()) {
            System.out.println("No pending company representatives to authorize.");
            return;
        }
        System.out.println("--- Pending Representatives ---");
        pendingReps.forEach(r -> System.out.println("ID: " + r.getUserID() + ", Name: " + r.getName() + ", Company: " + r.getCompanyName()));
        System.out.print("Enter Representative ID to process: ");
        String repId = scanner.nextLine();
        CompanyRepresentative rep = userManager.findRepresentativeById(repId);

        if (rep != null) {
            System.out.print("Approve (A) or Reject (R)? ");
            String action = scanner.nextLine().toUpperCase();
            if (action.equals("A")) {
                userManager.authorizeRepresentative(rep, true);
                System.out.println("Representative authorized.");
            } else if (action.equals("R")) {
                userManager.authorizeRepresentative(rep, false);
                System.out.println("Representative rejected.");
            } else {
                System.out.println("Invalid action.");
            }
        } else {
            System.out.println("Representative not found.");
        }
    }

    private void processInternship(Scanner scanner) {
        List<Internship> pendingInternships = internshipManager.getPendingInternships();
        if (pendingInternships.isEmpty()) {
            System.out.println("No pending internships to approve.");
            return;
        }
        System.out.println("--- Pending Internships ---");
        pendingInternships.forEach(i -> System.out.println("ID: " + i.getInternshipID() + ", Title: " + i.getTitle()));
        System.out.print("Enter Internship ID to process: ");
        String internId = scanner.nextLine();
        Internship internship = internshipManager.findInternshipById(internId);

        if (internship != null) {
            System.out.print("Approve (A) or Reject (R)? ");
            String action = scanner.nextLine().toUpperCase();
            if (action.equals("A")) {
                internshipManager.approveInternship(internship);
                System.out.println("Internship approved and is now open.");
            } else if (action.equals("R")) {
                internshipManager.rejectInternship(internship);
                System.out.println("Internship rejected.");
            } else {
                System.out.println("Invalid action.");
            }
        } else {
            System.out.println("Internship not found.");
        }
    }

    private void processWithdrawal(Scanner scanner) {
        List<WithdrawalRequest> pendingRequests = applicationManager.getPendingWithdrawalRequests();
        if (pendingRequests.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }
        System.out.println("--- Pending Withdrawal Requests ---");
        pendingRequests.forEach(r -> System.out.println("ID: " + r.getRequestID() + ", Student: " + r.getApplication().getStudent().getName()));
        System.out.print("Enter Request ID to process: ");
        String reqId = scanner.nextLine();
        WithdrawalRequest request = applicationManager.findRequestById(reqId);

        if (request != null) {
            System.out.print("Approve (A) or Reject (R)? ");
            String action = scanner.nextLine().toUpperCase();
            if (action.equals("A")) {
                applicationManager.processWithdrawalRequest(request, true);
                System.out.println("Withdrawal request approved.");
            } else if (action.equals("R")) {
                applicationManager.processWithdrawalRequest(request, false);
                System.out.println("Withdrawal request rejected.");
            } else {
                System.out.println("Invalid action.");
            }
        } else {
            System.out.println("Request not found.");
        }
    }
}