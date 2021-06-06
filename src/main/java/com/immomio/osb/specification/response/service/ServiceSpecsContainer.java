package com.immomio.osb.specification.response.service;

import lombok.Getter;

import java.util.List;

@Getter
public class ServiceSpecsContainer {
    private final List<ServiceSpec> services;

    public ServiceSpecsContainer(List<ServiceSpec> services) {
        this.services = services;
    }
}
