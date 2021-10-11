package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffAlert {

@SerializedName("mid")
@Expose
private Integer mid;
@SerializedName("mtitle")
@Expose
private String mtitle;
@SerializedName("message")
@Expose
private String message;
@SerializedName("read_status")
@Expose
private String readStatus;
@SerializedName("createon")
@Expose
private String createon;

public Integer getMid() {
return mid;
}

public void setMid(Integer mid) {
this.mid = mid;
}

public String getMtitle() {
return mtitle;
}

public void setMtitle(String mtitle) {
this.mtitle = mtitle;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public String getReadStatus() {
return readStatus;
}

public void setReadStatus(String readStatus) {
this.readStatus = readStatus;
}

public String getCreateon() {
return createon;
}

public void setCreateon(String createon) {
this.createon = createon;
}

}