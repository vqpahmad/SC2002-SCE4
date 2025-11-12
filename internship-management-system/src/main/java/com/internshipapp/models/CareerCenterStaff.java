package com.internshipapp.models;

import com.internshipapp.enums.*;

/**
 * Career center staff user. Can approve/reject internships, authorize
 * company representatives and process withdrawal requests.
 */
public class CareerCenterStaff extends User {
    /**
     * Create a CareerCenterStaff user.
     *
     * @param userID unique id
     * @param name display name
     * @param password account password
     * @param staffDepartment staff department name (currently unused)
     */
    public CareerCenterStaff(String userID, String name, String password, String staffDepartment) {
        super(userID, name, password);
    }

    /**
     * Authorize or de-authorize a company representative.
     *
     * @param rep representative to modify
     * @param isAuthorized true to authorize, false to deauthorize
     */
    public void authorizeCompanyRep(CompanyRepresentative rep, boolean isAuthorized) {
        // Logic to authorize or deauthorize a company representative
        rep.setApproved(isAuthorized);
    }

    /**
     * Approve an internship posting.
     *
     * @param internship internship to approve
     */
    public void approveInternship(Internship internship) {
        // Logic to approve or reject an internship
        internship.setStatus(InternshipStatus.APPROVED);
    }

    /**
     * Reject an internship posting.
     *
     * @param internship internship to reject
     * @param isRejected currently unused flag
     */
    public void rejectInternship(Internship internship, boolean isRejected) {
        // Logic to reject an internship
        internship.setStatus(InternshipStatus.REJECTED);
    }

    /**
     * Process a withdrawal request.
     *
     * @param request the withdrawal request
     * @param isApproved true to approve, false to reject
     */
    public void approveWithdrawal(WithdrawalRequest request, boolean isApproved) {
        // Logic to approve or reject a withdrawal request
        request.setStatus(isApproved ? RequestStatus.APPROVED : RequestStatus.REJECTED);
    }

    /**
     * Reject or reverse a withdrawal request.
     *
     * @param request the withdrawal request
     * @param isRejected boolean flag (semantics mirrored to approveWithdrawal)
     */
    public void rejectWithdrawal(WithdrawalRequest request, boolean isRejected) {
        // Logic to reject a withdrawal request
        request.setStatus(isRejected ? RequestStatus.REJECTED : RequestStatus.APPROVED);
    }

    /**
     * Generate a report object using provided filters.
     *
     * @param filters free-form filters string (implementation placeholder)
     * @return a Report instance
     */
    public Report generateReport(String filters) {
        // Logic to generate a report based on filters
        return new Report(filters); // Placeholder return
    }
}