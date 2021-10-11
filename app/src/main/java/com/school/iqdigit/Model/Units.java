package com.school.iqdigit.Model;

public class Units {
    Integer unit_id;

    public Units(String unit_name) {
        this.unit_name = unit_name;
    }


    public Integer getUnit_id() {
        return unit_id;
    }

    public String getUnit_name() {
        return unit_name;
    }

    String unit_name;

    public Units(Integer unit_id,String unit_name){
        this.unit_id = unit_id;
        this.unit_name = unit_name;
    }
}
