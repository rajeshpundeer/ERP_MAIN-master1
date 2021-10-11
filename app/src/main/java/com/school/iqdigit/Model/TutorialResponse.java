package com.school.iqdigit.Model;

import java.util.List;

public class TutorialResponse {
    private boolean error;

    private List<Tutorials> tutorials;

    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setTutorials(List<Tutorials> tutorials){
        this.tutorials = tutorials;
    }
    public List<Tutorials> getTutorials(){
        return this.tutorials;
    }
}
