package com.school.iqdigit.Modeldata;

public class SpinnerItems {
    String day;

    public SpinnerItems(String day, String returnDay) {
        this.day = day;
        this.returnDay = returnDay;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(String returnDay) {
        this.returnDay = returnDay;
    }

    String returnDay;
}
