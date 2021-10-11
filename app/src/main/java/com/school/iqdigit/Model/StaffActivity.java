package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StaffActivity {

@SerializedName("title")
@Expose
private String title;
@SerializedName("description")
@Expose
private String description;
@SerializedName("imgaddress")
@Expose
private String imgaddress;
@SerializedName("createon")
@Expose
private String createon;
@SerializedName("upto")
@Expose
private String upto;

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public String getImgaddress() {
return imgaddress;
}

public void setImgaddress(String imgaddress) {
this.imgaddress = imgaddress;
}

public String getCreateon() {
return createon;
}

public void setCreateon(String createon) {
this.createon = createon;
}

public String getUpto() {
return upto;
}

public void setUpto(String upto) {
this.upto = upto;
}

}