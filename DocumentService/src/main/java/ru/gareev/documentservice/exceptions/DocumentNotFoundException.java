package ru.gareev.documentservice.exceptions;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String message, Exception ex) {
        super(message, ex);
    }
}
