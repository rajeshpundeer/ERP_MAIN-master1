package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.TodayNall;

import java.util.List;

public class AllTodayNoticeResponse {
    private boolean error;
    private List<TodayNall> get_all_today_notice;

    public boolean isError() {
        return error;
    }

    public List<TodayNall> getGet_all_today_notice() {
        return get_all_today_notice;
    }

    public AllTodayNoticeResponse(boolean error, List<TodayNall> get_all_today_notice) {
        this.error = error;
        this.get_all_today_notice = get_all_today_notice;
    }
}
