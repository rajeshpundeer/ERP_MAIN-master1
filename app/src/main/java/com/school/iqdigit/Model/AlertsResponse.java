package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Alerts;

import java.util.List;

public class AlertsResponse {
    private boolean error;
    private List<Alerts> alerts;

    public boolean isError() {
        return error;
    }

    public List<Alerts> getAlerts() {
        return alerts;
    }

    public AlertsResponse(boolean error, List<Alerts> alerts) {
        this.error = error;
        this.alerts = alerts;
    }
}
