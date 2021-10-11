package com.school.iqdigit.Modeldata;

public class Markslist {
    private String m_total,m_pass,m_obtain,sub_name;

    public String getM_total() {
        return m_total;
    }

    public String getM_pass() {
        return m_pass;
    }

    public String getM_obtain() {
        return m_obtain;
    }

    public String getSub_name() {
        return sub_name;
    }

    public Markslist(String m_total, String m_pass, String m_obtain, String sub_name) {
        this.m_total = m_total;
        this.m_pass = m_pass;
        this.m_obtain = m_obtain;
        this.sub_name = sub_name;
    }
}
