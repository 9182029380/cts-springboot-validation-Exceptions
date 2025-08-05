package com.cts.trainers_application.exception;

public class InvalidTrainerDataException extends RuntimeException {
    public InvalidTrainerDataException(String message) {
        super(message);
    }

    public InvalidTrainerDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
