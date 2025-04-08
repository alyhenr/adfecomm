package com.adfecomm.adfecomm.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    Long fieldId;
    String resourceName;
    String field;
    String fieldName;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));

        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(Long fieldId, String resourceName, String field) {
        super(String.format("%s not found with %s: %d", resourceName, field, fieldId));

        this.fieldId = fieldId;
        this.resourceName = resourceName;
        this.field = field;
    }
}
