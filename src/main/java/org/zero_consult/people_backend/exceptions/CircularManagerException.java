package org.zero_consult.people_backend.exceptions;

public class CircularManagerException extends Exception {
    public CircularManagerException(String message) {
        super(message);
    }
}
