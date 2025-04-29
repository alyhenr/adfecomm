package com.adfecomm.adfecomm.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    Long fieldId;
    String resourceName;
    String field;
    String fieldName;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(Long fieldId, String resourceName, String field) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));

        this.fieldId = fieldId;
        this.resourceName = resourceName;
        this.field = field;
    }

    public ResourceNotFoundException(String resourceName) {
        super(String.format("%s not found", resourceName));

        this.resourceName = resourceName;
    }
}
