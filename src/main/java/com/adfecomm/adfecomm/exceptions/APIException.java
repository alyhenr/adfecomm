package com.adfecomm.adfecomm.exceptions;

public class APIException extends RuntimeException {
    private static final long seriaVersionUID = 1L;

    public APIException() {
    }

    public APIException(String message) {
        super(message);
    }
}
