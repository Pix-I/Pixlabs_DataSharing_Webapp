package com.pixlabs.web.util;

/**
 * Created by pix-i on 19/01/2017.
 * ${Copyright}
 */
public class GenericResponse {

    private String message;
    private String error;

    public GenericResponse(String message) {
        this.message = message;
    }

    public GenericResponse(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public GenericResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getError() {
        return error;
    }

    public GenericResponse setError(String error) {
        this.error = error;
        return this;
    }
}
