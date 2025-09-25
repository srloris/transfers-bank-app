package com.acme.transfers.backend.services.Exceptions;

public class InvalidTransferDateException extends RuntimeException {
    public InvalidTransferDateException(String message) {
        super(message);
    }
}
