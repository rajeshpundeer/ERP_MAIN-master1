package com.school.iqdigit.Model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AchievementResponse {
    private Boolean error;
    private List<Achievement_> achievements = null;

    public AchievementResponse(boolean error, List<Achievement_> achievements) {
        this.error = error;
        this.achievements = achievements;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<Achievement_> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement_> achievements) {
        this.achievements = achievements;
    }

}