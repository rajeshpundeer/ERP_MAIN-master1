package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Activities;

import java.util.List;

public class ActivitiesResponse {
    private  boolean error;
    private List<Activities> user_activities ;

    public boolean isError() {
        return error;
    }

    public List<Activities> getUser_activities() {
        return user_activities;
    }

    public ActivitiesResponse(boolean error, List<Activities> user_activities) {
        this.error = error;
        this.user_activities = user_activities;
    }
}
