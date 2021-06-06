package com.immomio.osb.specification.response.dto;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Getter
public class BindingResponse {
    private String password;
    private String uri;
    private String username;

    @Getter(value = AccessLevel.NONE)
    private final HttpStatus httpStatus;

    public BindingResponse(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BindingResponse(String instanceId, String username, HttpStatus httpStatus) {
        this.password = UUID.randomUUID().toString().replace("-", "");
        this.uri = String.format("https://some-service/%s", instanceId);;
        this.username = username;
        this.httpStatus = httpStatus;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

}
