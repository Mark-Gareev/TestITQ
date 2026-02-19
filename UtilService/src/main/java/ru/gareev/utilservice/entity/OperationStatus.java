package ru.gareev.utilservice.entity;

public enum OperationStatus {
    SUCCESS, FAILURE, NOT_FOUND;

    public static OperationStatus forString(String s) {
        switch (s) {
            case "успешно" -> {
                return SUCCESS;
            }
            case "конфликт" -> {
                return FAILURE;
            }
            case "не найдено" -> {
                return NOT_FOUND;
            }
            default -> throw new UnsupportedOperationException();
        }
    }
}
