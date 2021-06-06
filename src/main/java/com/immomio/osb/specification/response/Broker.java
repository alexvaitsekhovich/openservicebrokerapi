package com.immomio.osb.specification.response;

import com.immomio.osb.specification.request.dto.ServiceProvisioningDto;
import com.immomio.osb.specification.response.dto.BindingResponse;
import com.immomio.osb.specification.response.dto.DeleteOperation;
import com.immomio.osb.specification.response.dto.ProvisioningResponse;
import com.immomio.osb.specification.response.service.ServiceSpecsContainer;

public interface Broker {
    ServiceSpecsContainer getServices();

    ProvisioningResponse provisionService(ServiceProvisioningDto servicePlan, String instanceId);
    DeleteOperation deprovisionService(ServiceProvisioningDto servicePlan, String instanceId);

    BindingResponse bindService(ServiceProvisioningDto servicePlan, String instanceId, String bindingId);
    DeleteOperation unbindService(ServiceProvisioningDto serviceProvisioningDto, String instanceId, String bindingId);
}
