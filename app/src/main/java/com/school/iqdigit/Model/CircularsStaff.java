package com.school.iqdigit.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CircularsStaff {

@SerializedName("img")
@Expose
private String img;
@SerializedName("title")
@Expose
private String title;
@SerializedName("description")
@Expose
private String description;

public String getImg() {
return img;
}

public void setImg(String img) {
this.img = img;
}

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

}