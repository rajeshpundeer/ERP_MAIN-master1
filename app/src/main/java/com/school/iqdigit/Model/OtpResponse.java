package com.school.iqdigit.Model;

public class OtpResponse {
    private boolean error;
    private String helpnum;

    public String getAdmission_helpline() {
        return admission_helpline;
    }

    public void setAdmission_helpline(String admission_helpline) {
        this.admission_helpline = admission_helpline;
    }

    private String admission_helpline;



    public boolean isError() {
        return error;
    }

    public String getHelpnum() {
        return helpnum;
    }

    public OtpResponse(boolean error) {

        this.error = error;
    }
}
