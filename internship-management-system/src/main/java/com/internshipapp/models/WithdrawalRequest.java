package com.internshipapp.models;

import com.internshipapp.enums.*;
import java.util.Date;

/**
 * A request created by a student to withdraw an application.
 */
public class WithdrawalRequest {
    private String requestID;
    private Application application;
    private Date requestDate;
    private RequestStatus status;

    /**
     * Create a new withdrawal request for an application.
     *
     * @param application the application to withdraw from
     */
    public WithdrawalRequest(Application application) {
        this.requestID = java.util.UUID.randomUUID().toString();
        this.application = application;
        this.requestDate = new Date();
        this.status = RequestStatus.PENDING;
    }

    /**
     * Returns the unique identifier for this withdrawal request.
     *
     * @return the unique request identifier
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Returns the application associated with this withdrawal request.
     *
     * @return the application associated with this request
     */
    public Application getApplication() {
        return application;
    }

    /**
     * Returns the current approval status of the withdrawal request.
     *
     * @return the current status of the request
     */
    public RequestStatus getStatus() {
        return status;
    }

    /**
     * Returns the date this withdrawal request was created.
     *
     * @return the date the request was created
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Update the status of the withdrawal request.
     *
     * @param status new request status
     */
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}