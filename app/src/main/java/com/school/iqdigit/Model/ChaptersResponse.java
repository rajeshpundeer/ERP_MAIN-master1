package com.school.iqdigit.Model;

import java.util.List;

public class ChaptersResponse {
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    private boolean error;

    public List<Chapters> getChapters() {
        return chapters;
    }

    private List<Chapters> chapters;
}

