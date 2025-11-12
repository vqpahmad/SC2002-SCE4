package com.internshipapp.enums;

/**
 * Status for administrative requests such as withdrawal requests.
 */
public enum RequestStatus {
    /** Request is awaiting processing. */
    PENDING,
    /** Request has been approved. */
    APPROVED,
    /** Request was rejected. */
    REJECTED
}