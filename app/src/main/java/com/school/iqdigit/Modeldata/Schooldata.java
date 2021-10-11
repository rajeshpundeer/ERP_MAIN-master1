package com.school.iqdigit.Modeldata;

public class Schooldata {
    private String logo,sch_cname,sch_add;

    public String getLogo() {
        return logo;
    }

    public String getSch_cname() {
        return sch_cname;
    }

    public String getSch_add() {
        return sch_add;
    }

    public Schooldata(String logo, String sch_cname, String sch_add) {
        this.logo = logo;
        this.sch_cname = sch_cname;
        this.sch_add = sch_add;
    }
}
