package com.school.iqdigit.Model;

import com.school.iqdigit.Modeldata.Gatewaydata;

public class GatewaydataResponse {
    private boolean error;
    private Gatewaydata gateway_deatils;

    public boolean isError() {
        return error;
    }

    public Gatewaydata getGateway_deatils() {
        return gateway_deatils;
    }

    public GatewaydataResponse(boolean error, Gatewaydata gateway_deatils) {
        this.error = error;
        this.gateway_deatils = gateway_deatils;
    }
}
