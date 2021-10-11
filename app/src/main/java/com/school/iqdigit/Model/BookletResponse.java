package com.school.iqdigit.Model;

import java.util.List;

public class BookletResponse {
    private boolean error;

    private List<Booklets> booklets;

    public void setError(boolean error){
        this.error = error;
    }
    public boolean getError(){
        return this.error;
    }
    public void setBooklets(List<Booklets> booklets){
        this.booklets = booklets;
    }
    public List<Booklets> getBooklets(){
        return this.booklets;
    }
}
