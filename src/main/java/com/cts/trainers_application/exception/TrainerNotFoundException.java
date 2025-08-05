package com.cts.trainers_application.exception;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(String message) {
        super(message);
    }

    public TrainerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrainerNotFoundException(Long id) {
        super("Trainer not found with id: " + id);
    }
}

