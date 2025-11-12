package com.internshipapp.enums;

/**
 * Lifecycle status of an internship posting.
 */
public enum InternshipStatus {
    /** Internship is awaiting approval from staff. */
    PENDING,
    /** Internship has been approved and is open for applications. */
    APPROVED,
    /** Internship was rejected by staff. */
    REJECTED,
    /** Internship slots have been filled. */
    FILLED
}