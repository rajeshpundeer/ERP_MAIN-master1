package com.school.iqdigit.Modeldata;

public class Alerts {
    private String mid, mtitle, message, read_status, createon;

    public String getMid() {
        return mid;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getMessage() {
        return message;
    }

    public String getRead_status() {
        return read_status;
    }

    public String getCreateon() {
        return createon;
    }

    public Alerts(String mid, String mtitle, String message, String read_status, String createon) {
        this.mid = mid;
        this.mtitle = mtitle;
        this.message = message;
        this.read_status = read_status;
        this.createon = createon;
    }
}
