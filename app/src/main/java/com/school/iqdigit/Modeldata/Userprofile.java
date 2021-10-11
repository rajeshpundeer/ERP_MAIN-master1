package com.school.iqdigit.Modeldata;

public class Userprofile {
    //    private String id , phone_number ,name;
//
//    public Userprofile(String id, String phone_number, String name) {
//        this.id = id;
//        this.phone_number = phone_number;
//        this.name = name;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public String getPhone_number() {
//        return phone_number;
//    }
//
//    public String getName() {
//        return name;
//    }
    private String id;

    public String getClassid() {
        return classid;
    }

    private String classid;

    public Userprofile(String id, String phonenumber, String name, String adm_no, String dob, String classid, String stud_lname, String father_name, String mother_name, String class_r, String roll_no, String adm_type, String result_status, String stud_status, String caste, String religion, String gender, String address, String adarcard, String adm_date, String inactive_date, String pickup_point, String photo) {
        this.id = id;
        this.phonenumber = phonenumber;
        this.name = name;
        this.adm_no = adm_no;
        this.dob = dob;
//        this.middle_name = middle_name;
        this.stud_lname = stud_lname;
        this.father_name = father_name;
        this.mother_name = mother_name;
        this.class_r = class_r;
        this.roll_no = roll_no;
        this.adm_type = adm_type;
        this.result_status = result_status;
        this.stud_status = stud_status;
        this.caste = caste;
        this.religion = religion;
        this.gender = gender;
        this.address = address;
        this.adarcard = adarcard;
        this.adm_date = adm_date;
        this.inactive_date = inactive_date;
        this.pickup_point = pickup_point;
        this.photo = photo;
        this.classid = classid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getName() {
        return name;
    }

    public String getAdm_no() {
        return adm_no;
    }


    public String getDob() {
        return dob;
    }

//    public String getMiddle_name() {
//        return middle_name;
//    }

    public String getStud_lname() {
        return stud_lname;
    }

    public String getFather_name() {
        return father_name;
    }

    public String getMother_name() {
        return mother_name;
    }

    public String getClass_r() {
        return class_r;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public String getAdm_type() {
        return adm_type;
    }

    public String getResult_status() {
        return result_status;
    }

    public String getStud_status() {
        return stud_status;
    }

    public String getCaste() {
        return caste;
    }

    public String getReligion() {
        return religion;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getAadhar_no() {
        return adarcard;
    }

    public String getAdm_date() {
        return adm_date;
    }

    public String getInactive_date() {
        return inactive_date;
    }

    public String getPickup_point() {
        return pickup_point;
    }

    public String getPhoto() {
        return photo;
    }

    private String phonenumber;
    private String name;
    private String adm_no;
    private String dob;
    //    private String middle_name;
    private String stud_lname;
    private String father_name;
    private String mother_name;
    private String class_r;
    private String roll_no;
    private String adm_type;
    private String result_status;
    private String stud_status;
    private String caste;
    private String religion;
    private String gender;
    private String address;
    private String adarcard;
    private String adm_date;
    private String inactive_date;
    private String pickup_point;
    private String photo;

    public String getBloodgroup() {
        return bloodgroup;
    }

    private String bloodgroup;

    public Userprofile(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }


}
