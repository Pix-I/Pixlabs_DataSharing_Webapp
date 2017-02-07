package com.pixlabs.exceptions;

/**
 * Created by pix-i on 07/02/2017.
 * ${Copyright}
 */
public class PermissionDeniedException extends RuntimeException {
    private static final  long serialVersionUID = 5861310537366287L;

    public PermissionDeniedException() {
        super();
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
