package com.school.iqdigit.Model;

public class ErrorResponse {
    private boolean error;

    public boolean isError() {
        return error;
    }

    public ErrorResponse(boolean error) {
        this.error = error;
    }
}
