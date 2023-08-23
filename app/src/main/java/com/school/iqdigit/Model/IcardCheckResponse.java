package com.school.iqdigit.Model;

public class IcardCheckResponse {
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public PhotoCheck getPhoto_check() {
        return photo_check;
    }

    public void setPhoto_check(PhotoCheck photo_check) {
        this.photo_check = photo_check;
    }

    public boolean error;
    public PhotoCheck photo_check;
}