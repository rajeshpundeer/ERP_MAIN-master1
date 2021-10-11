package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.todaynotice;

public class noticeresponse {
    private boolean error;
    private todaynotice notice;

    public boolean isError() {
        return error;
    }

    public todaynotice getNotice() {
        return notice;
    }

    public noticeresponse(boolean error, todaynotice notice) {
        this.error = error;
        this.notice = notice;
    }
}
