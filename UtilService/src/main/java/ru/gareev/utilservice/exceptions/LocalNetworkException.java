package ru.gareev.utilservice.exceptions;

public class LocalNetworkException extends RuntimeException {
    public LocalNetworkException() {
        super();
    }

    public LocalNetworkException(String message, Exception e) {
        super(message, e);
    }

    public LocalNetworkException(String message) {
        super(message);
    }
}
