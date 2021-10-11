package com.school.iqdigit.Model;

import java.util.List;

public class MediaRespose
{
    private boolean error;

    private List<Media> media;

    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setMedia(List<Media> media){
        this.media = media;
    }
    public List<Media> getMedia(){
        return this.media;
    }
}