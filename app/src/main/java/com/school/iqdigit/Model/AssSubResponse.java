package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.AssSub;

import java.util.List;

public class AssSubResponse {
    private boolean error;
    private List<AssSub> user_assignment;

    public boolean isError() {
        return error;
    }

    public List<AssSub> getUser_assignment() {
        return user_assignment;
    }

    public AssSubResponse(boolean error, List<AssSub> user_assignment) {
        this.error = error;
        this.user_assignment = user_assignment;
    }
}
