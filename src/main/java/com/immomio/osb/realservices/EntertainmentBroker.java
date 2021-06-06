package com.immomio.osb.realservices;

import com.immomio.osb.realservices.services.ServiceBinding;
import com.immomio.osb.realservices.services.ServicePlan;
import com.immomio.osb.specification.exception.service.ServiceBindingException;
import com.immomio.osb.specification.exception.service.ServicePlanException;
import com.immomio.osb.specification.request.dto.ServiceProvisioningDto;
import com.immomio.osb.specification.response.Broker;
import com.immomio.osb.specification.response.dto.BindingResponse;
import com.immomio.osb.specification.response.dto.DeleteOperation;
import com.immomio.osb.specification.response.dto.ProvisioningResponse;
import com.immomio.osb.specification.response.service.ServicePlanSpec;
import com.immomio.osb.specification.response.service.ServiceSpec;
import com.immomio.osb.specification.response.service.ServiceSpecsContainer;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EntertainmentBroker implements Broker {

    private final ServiceSpecsContainer servicesContainer;
    private final Map<String, ServicePlan> provisionedServices;
    private final Map<ServiceBinding, ServicePlan> serviceBindings;

    public EntertainmentBroker() {
        provisionedServices = new HashMap<>();
        serviceBindings = new HashMap<>();

        var services = initServices();
        servicesContainer = new ServiceSpecsContainer(services);
    }

    private List<ServiceSpec> initServices() {
        List<ServiceSpec> services = new ArrayList<>();

        ServiceSpec streamingService = new ServiceSpec("11", "Streaming", "Streaming movie service");
        streamingService.addPlan(new ServicePlanSpec("11A", "free", "Free streaming plan"));
        services.add(streamingService);

        ServiceSpec mailingService = new ServiceSpec("22", "Mailing", "E-Mail service");
        mailingService.addPlan(new ServicePlanSpec("22A", "free", "Free mailing plan"));
        mailingService.addPlan(new ServicePlanSpec("22B", "premium", "Premium mailing plan"));
        mailingService.addPlan(new ServicePlanSpec("22C", "business", "Business mailing plan"));
        services.add(mailingService);
        return services;
    }

    @Override
    public ServiceSpecsContainer getServices() {
        return servicesContainer;
    }

    @SneakyThrows
    @Override
    public ProvisioningResponse provisionService(ServiceProvisioningDto servicePlanDto, String instanceId) {

        ServicePlan plan = checkServicePlanDtoSanity(servicePlanDto);

        ServicePlan existingServicePlan = provisionedServices.get(instanceId);

        if (existingServicePlan != null) {
            HttpStatus checkStatus = checkServicePlanClash(existingServicePlan, plan);
            if (checkStatus == HttpStatus.OK) {
                return new ProvisioningResponse(instanceId, HttpStatus.OK);
            }

            return new ProvisioningResponse(checkStatus);
        }

        provisionedServices.put(instanceId, plan);
        return new ProvisioningResponse(instanceId, HttpStatus.CREATED);
    }

    @SneakyThrows
    @Override
    public DeleteOperation deprovisionService(ServiceProvisioningDto servicePlanDto, String instanceId) {

        if (!provisionedServices.containsKey(instanceId)) {
            throw new ServicePlanException(String.format("Service plan for instance %s is not provisioned", instanceId));
        }

        ServicePlan servicePlan = provisionedServices.get(instanceId);

        if (!servicePlan.getServiceId().equals(servicePlanDto.getServiceId())
                || !servicePlan.getPlanId().equals(servicePlanDto.getPlanId())) {
            throw new ServicePlanException("Service plan provisioned, but has different parameters");
        }

        provisionedServices.remove(instanceId);

        return new DeleteOperation(DeleteOperation.DELETE_OPERATION_PLACEHOLDER);
    }

    @SneakyThrows
    @Override
    public BindingResponse bindService(ServiceProvisioningDto servicePlanDto, String instanceId, String bindingId) {
        ServicePlan plan = checkServicePlanDtoSanity(servicePlanDto);

        ServiceBinding serviceBinding = new ServiceBinding(instanceId, bindingId);

        ServicePlan existingBindingServicePlan = serviceBindings.get(serviceBinding);

        if (existingBindingServicePlan != null) {
            HttpStatus checkStatus = checkServicePlanClash(existingBindingServicePlan, plan);
            if (checkStatus == HttpStatus.OK) {
                return new BindingResponse(instanceId, bindingId, HttpStatus.OK);
            }

            return new BindingResponse(checkStatus);
        }

        serviceBindings.put(serviceBinding, plan);
        return new BindingResponse(instanceId, bindingId, HttpStatus.CREATED);
    }

    @SneakyThrows
    @Override
    public DeleteOperation unbindService(ServiceProvisioningDto serviceProvisioningDto, String instanceId, String bindingId) {
        ServiceBinding serviceBinding = new ServiceBinding(instanceId, bindingId);

        if (!serviceBindings.containsKey(serviceBinding)) {
            throw new ServiceBindingException(String.format("No service binding for instanceId %s and bindingId %s found", instanceId, bindingId));
        }

        ServicePlan servicePlan = serviceBindings.get(serviceBinding);

        if (!servicePlan.getServiceId().equals(serviceProvisioningDto.getServiceId())
                || !servicePlan.getPlanId().equals(serviceProvisioningDto.getPlanId())) {
            throw new ServicePlanException("Service binding found, but has different parameters");
        }

        serviceBindings.remove(serviceBinding);
        return new DeleteOperation(DeleteOperation.DELETE_OPERATION_PLACEHOLDER);

    }

    private ServicePlan checkServicePlanDtoSanity(ServiceProvisioningDto servicePlanDto) throws ServicePlanException {
        String serviceId = servicePlanDto.getServiceId();
        String planId = servicePlanDto.getPlanId();

        Optional<ServiceSpec> service = servicesContainer.getServices().stream()
                .filter(s -> s.getId().equals(serviceId)).findFirst();

        if (service.isEmpty()) {
            throw new ServicePlanException("Service Id not found");
        }

        if (service.get().getPlans().stream().noneMatch(p -> p.getId().equals(planId))) {
            throw new ServicePlanException("Plan Id not found");
        }

        return new ServicePlan(serviceId, planId);
    }

    private HttpStatus checkServicePlanClash(ServicePlan existingBindingServicePlan, ServicePlan newPlan) {
        if (!existingBindingServicePlan.getServiceId().equals(newPlan.getServiceId())
                || !existingBindingServicePlan.getPlanId().equals(newPlan.getPlanId())) {
            return HttpStatus.CONFLICT;
        }

        return HttpStatus.OK;
    }
}
