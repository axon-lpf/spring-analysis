package com.axon.springframework.core.annotation;

public class AnnotationConfigurationException extends RuntimeException {

    public AnnotationConfigurationException(String message) {
        super(message);
    }

    public AnnotationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
