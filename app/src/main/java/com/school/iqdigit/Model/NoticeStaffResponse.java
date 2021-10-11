package com.school.iqdigit.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NoticeStaffResponse {

@SerializedName("error")
@Expose
private Boolean error;
@SerializedName("get_all_today_notice_staff")
@Expose
private List<GetAllTodayNoticeStaff> getAllTodayNoticeStaff = null;

public Boolean getError() {
return error;
}

public void setError(Boolean error) {
this.error = error;
}

public List<GetAllTodayNoticeStaff> getGetAllTodayNoticeStaff() {
return getAllTodayNoticeStaff;
}

public void setGetAllTodayNoticeStaff(List<GetAllTodayNoticeStaff> getAllTodayNoticeStaff) {
this.getAllTodayNoticeStaff = getAllTodayNoticeStaff;
}

}