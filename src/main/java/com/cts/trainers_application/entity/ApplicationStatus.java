package com.cts.trainers_application.entity;

public enum ApplicationStatus {
    PENDING("Pending Review"),
    UNDER_REVIEW("Under Review"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    ON_HOLD("On Hold"),
    INTERVIEW_SCHEDULED("Interview Scheduled"),
    HIRED("Hired");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
