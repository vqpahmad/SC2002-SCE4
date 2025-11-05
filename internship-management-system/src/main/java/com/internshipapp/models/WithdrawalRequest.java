package com.internshipapp.models;

import com.internshipapp.enums.*;
import java.util.Date;

public class WithdrawalRequest {
    private String requestID;
    private Application application;
    private Date requestDate;
    private RequestStatus status;

    public WithdrawalRequest(Application application) {
        this.requestID = java.util.UUID.randomUUID().toString();
        this.application = application;
        this.requestDate = new Date();
        this.status = RequestStatus.PENDING;
    }

    public String getRequestID() {
        return requestID;
    }

    public Application getApplication() {
        return application;
    }
    public RequestStatus getStatus() {
        return status;
    }
    public Date getRequestDate() {
        return requestDate;
    }
    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}