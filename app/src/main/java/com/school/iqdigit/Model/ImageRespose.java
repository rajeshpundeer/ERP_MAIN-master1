package com.school.iqdigit.Model;

import java.util.List;

public class ImageRespose
{
    private boolean error;

    private List<Image> image;

    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setImage(List<Image> image){
        this.image = image;
    }
    public List<Image> getImage(){
        return this.image;
    }
}