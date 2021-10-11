package com.school.iqdigit.Modeldata;

public class User {
    private String id;
    private String phone_number;
    private String name;
    private String p_class;
    private String incharge_id; ;


    public User(String id, String phone_number, String name, String p_class, String incharge_id) {
        this.id = id;
        this.phone_number = phone_number;
        this.name = name;
        this.p_class = p_class;
        this.incharge_id = incharge_id;
    }

    public String getId() {
        return id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getName() {
        return name;
    }

    public String getP_class() {
        return p_class;
    }

    public String getIncharge_id() { return incharge_id; }
}
