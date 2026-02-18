package ru.gareev.documentservice.entity;

public enum DocumentStatus {
    DRAFT, SUBMITTED, APPROVED;

    public static DocumentStatus forString(String s) {
        if (s != null) {
            for (DocumentStatus status : DocumentStatus.values()) {
                if (s.equals(status.toString())) {
                    return status;
                }
            }
        }
        return null;
    }
}
