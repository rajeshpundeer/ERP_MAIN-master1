package com.school.iqdigit.Modeldata;

public class Feelistdata {
    private String paidamount;
    private String duration;
    private String feetype;
    private String dtype;

    public String getPaidamount() {
        return paidamount;
    }

    public String getDuration() {
        return duration;
    }

    public String getFeetype() {
        return feetype;
    }

    public String getDtype() {
        return dtype;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public Feelistdata(String paidamount, String duration, String feetype, String dtype, String totalamount) {
        this.paidamount = paidamount;
        this.duration = duration;
        this.feetype = feetype;
        this.dtype = dtype;
        this.totalamount = totalamount;
    }

    private String totalamount;


}
