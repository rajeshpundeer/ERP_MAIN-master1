package com.school.iqdigit.Model;

public class Check {
private String version;
private String maintenace;
private String m_message;
private String announcement;
private String stud_modules;
private String staff_modules;


    public String getStud_modules() {
        return stud_modules;
    }

    public void setStud_modules(String stud_modules) {
        this.stud_modules = stud_modules;
    }

    public String getStaff_modules() {
        return staff_modules;
    }

    public void setStaff_modules(String staff_modules) {
        this.staff_modules = staff_modules;
    }

    public String getM_message() {
        return m_message;
    }

    public void setM_message(String m_message) {
        this.m_message = m_message;
    }

    public String getMin_version() {
        return min_version;
    }

    public void setMin_version(String min_version) {
        this.min_version = min_version;
    }

    private String min_version;

public String getVersion() {
return version;
}

public void setVersion(String version) {
this.version = version;
}

public String getMaintenace() {
return maintenace;
}

public void setMaintenace(String maintenace) {
this.maintenace = maintenace;
}

public String getMMessage() {
return m_message;
}

public void setMMessage(String m_message) {
this.m_message = m_message;
}

public String getAnnouncement() {
return announcement;
}

public void setAnnouncement(String announcement) {
this.announcement = announcement;
}
}