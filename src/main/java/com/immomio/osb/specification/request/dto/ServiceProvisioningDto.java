package com.immomio.osb.specification.request.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProvisioningDto {

    @NotNull
    private String service_id;

    @NotNull
    private String plan_id;

    public String getServiceId() {
        return service_id;
    }

    public String getPlanId() {
        return plan_id;
    }
}
