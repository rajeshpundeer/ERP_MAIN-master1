package com.school.iqdigit.Model;

public class SubmitpaymentResponse {
    private boolean error;

    public boolean isError() {
        return error;
    }

    public SubmitpaymentResponse(boolean error) {
        this.error = error;
    }
}
