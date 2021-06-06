package com.immomio.osb.specification.response.dto;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProvisioningResponse {
    @Getter(value = AccessLevel.NONE)
    private final HttpStatus httpStatus;

    private String dashboard_url = "";

    public ProvisioningResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ProvisioningResponse(String instanceId, HttpStatus httpStatus) {
        this.dashboard_url = String.format("https://some-service/%s", instanceId);
        this.httpStatus = httpStatus;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
