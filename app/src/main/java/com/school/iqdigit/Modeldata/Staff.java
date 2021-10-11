package com.school.iqdigit.Modeldata;

public class Staff {
    private String id,phone_number,name,gender,fathername,dob,email,status,salery,dojoining,depname,postname,address,user_type,imgpath;

    public String getId() {
        return id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getFathername() {
        return fathername;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getSalery() {
        return salery;
    }

    public String getDojoining() {
        return dojoining;
    }

    public String getDepname() {
        return depname;
    }

    public String getPostname() {
        return postname;
    }

    public String getAddress() {
        return address;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getImgpath() {
        return imgpath;
    }

    public Staff(String id, String phone_number, String name, String gender, String fathername, String dob, String email, String status, String salery, String dojoining, String depname, String postname, String address, String user_type, String imgpath) {
        this.id = id;
        this.phone_number = phone_number;
        this.name = name;
        this.gender = gender;
        this.fathername = fathername;
        this.dob = dob;
        this.email = email;
        this.status = status;
        this.salery = salery;
        this.dojoining = dojoining;
        this.depname = depname;
        this.postname = postname;
        this.address = address;
        this.user_type = user_type;
        this.imgpath = imgpath;
    }
}
