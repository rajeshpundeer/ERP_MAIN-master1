package com.school.iqdigit.Modeldata;

public class Activities {
    private  String title, description, imgaddress ,createon, upto;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImgaddress() {
        return imgaddress;
    }

    public String getCreateon() {
        return createon;
    }

    public String getUpto() {
        return upto;
    }

    public Activities(String title, String description, String imgaddress, String createon, String upto) {
        this.title = title;
        this.description = description;
        this.imgaddress = imgaddress;
        this.createon = createon;
        this.upto = upto;
    }
}
