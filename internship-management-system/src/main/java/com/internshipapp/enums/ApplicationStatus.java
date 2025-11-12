package com.internshipapp.enums;

/**
 * Status of an application through the review lifecycle.
 */
public enum ApplicationStatus {
    /** Application submitted and awaiting review. */
    PENDING,
    /** Application approved by the company representative. */
    APPROVED,
    /** Student has accepted the placement offer. */
    ACCEPTED,
    /** Application was not successful (rejected or withdrawn). */
    UNSUCCESSFUL
}