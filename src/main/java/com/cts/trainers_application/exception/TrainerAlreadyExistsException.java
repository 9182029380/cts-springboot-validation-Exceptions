package com.cts.trainers_application.exception;

public class TrainerAlreadyExistsException extends RuntimeException {

    public TrainerAlreadyExistsException(String email) {
        super("Trainer with email " + email + " already exists");
    }
}
