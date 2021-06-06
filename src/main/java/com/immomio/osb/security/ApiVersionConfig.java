package com.immomio.osb.security;

import com.immomio.osb.specification.exception.configuration.BadConfigurationException;
import com.immomio.osb.specification.request.apiversion.ApiFormatException;
import com.immomio.osb.specification.request.apiversion.ApiVersion;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiVersionConfig {

    @Value("${immomio.osb.broker-api-version-min}")
    private String apiMinimalVersion;

    public static ApiVersion MIN_API_VERSION;

    @Value("${immomio.osb.broker-api-version-min}")
    public void setApiMinimalVersion(String minVersion) throws BadConfigurationException {
        try {
            ApiVersionConfig.MIN_API_VERSION = new ApiVersion(minVersion);
        } catch (ApiFormatException e) {
            throw new BadConfigurationException("Malformed API version in configuration");
        }
    }
}
