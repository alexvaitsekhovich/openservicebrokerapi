package com.immomio.osb.realservices.services;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ServiceBinding {
    private String instanceId;
    private String bindingId;
}
