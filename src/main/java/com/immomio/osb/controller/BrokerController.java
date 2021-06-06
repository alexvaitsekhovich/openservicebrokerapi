package com.immomio.osb.controller;

import com.immomio.osb.specification.request.dto.ServiceProvisioningDto;
import com.immomio.osb.specification.response.Broker;
import com.immomio.osb.specification.response.dto.BindingResponse;
import com.immomio.osb.specification.response.dto.DeleteOperation;
import com.immomio.osb.specification.response.dto.ProvisioningResponse;
import com.immomio.osb.specification.response.service.ServiceSpecsContainer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping()
public class BrokerController {

    private static final String CATALOG_URL = "/v2/catalog";
    private static final String SERVICE_URL = "/v2/service_instances/{instanceId}";
    private static final String BINDING_URL = "/v2/service_instances/{instanceId}/service_bindings/{bindingId}";

    private final Broker broker;

    public BrokerController(Broker broker) {
        this.broker = broker;
    }

    /*
        curl 'http://immomio:immomio@localhost:8080/v2/catalog' -H "X-Broker-API-Version: 2.16" | json_pp
     */

    @GetMapping(CATALOG_URL)
    public ServiceSpecsContainer getCatalog() {
        return broker.getServices();
    }

    /*
        curl 'http://immomio:immomio@localhost:8080/v2/service_instances/TestInstance' -d '{"service_id": "22","plan_id": "22A"}' -X PUT -H "X-Broker-API-Version: 2.16" -H "Content-Type: application/json"  | json_pp
     */
    @PutMapping(value = SERVICE_URL)
    public ResponseEntity<ProvisioningResponse> createServiceInstance(
            @Valid @RequestBody ServiceProvisioningDto servicePlan,
            @PathVariable String instanceId
    ) {
        ProvisioningResponse provisioningResponse = broker.provisionService(servicePlan, instanceId);

        if (provisioningResponse.httpStatus().is2xxSuccessful()) {
            return new ResponseEntity<>(provisioningResponse, provisioningResponse.httpStatus());
        }

        return new ResponseEntity<>(provisioningResponse.httpStatus());
    }

    /*
        curl 'http://immomio:immomio@localhost:8080/v2/service_instances/TestInstance?service_id=22&plan_id=22A' -X DELETE -H "X-Broker-API-Version: 2.16"  | json_pp
     */
    @DeleteMapping(SERVICE_URL)
    public ResponseEntity<DeleteOperation> deleteServiceInstance(
            @PathVariable String instanceId,
            @RequestParam("service_id") String serviceId,
            @RequestParam("plan_id") String planId
    ) {
        return new ResponseEntity<>(
                broker.deprovisionService(new ServiceProvisioningDto(serviceId, planId), instanceId),
                HttpStatus.OK
        );
    }

    /*
        curl 'http://immomio:immomio@localhost:8080/v2/service_instances/TestInstance/service_bindings/admin' -d '{"service_id": "22","plan_id": "22A"}' -X PUT -H "X-Broker-API-Version: 2.16" -H "Content-Type: application/json"  | json_pp
     */
    @PutMapping(BINDING_URL)
    public ResponseEntity<BindingResponse> bindService(
            @Valid @RequestBody ServiceProvisioningDto servicePlan,
            @PathVariable String instanceId,
            @PathVariable String bindingId) {

        BindingResponse bindingResponse = broker.bindService(servicePlan, instanceId, bindingId);

        if (bindingResponse.httpStatus().is2xxSuccessful()) {
            return new ResponseEntity<>(bindingResponse, bindingResponse.httpStatus());
        }

        return new ResponseEntity<>(bindingResponse.httpStatus());
    }

    /*
        curl 'http://immomio:immomio@localhost:8080/v2/service_instances/TestInstance/service_bindings/admin?service_id=22&plan_id=22A' -X DELETE -H "X-Broker-API-Version: 2.16"  | json_pp
     */
    @DeleteMapping(BINDING_URL)
    public ResponseEntity<DeleteOperation> unbindService(
            @PathVariable String instanceId,
            @PathVariable String bindingId,
            @RequestParam("service_id") String serviceId,
            @RequestParam("plan_id") String planId
    ) {
        return new ResponseEntity<>(
                broker.unbindService(new ServiceProvisioningDto(serviceId, planId), instanceId, bindingId),
                HttpStatus.OK
        );
    }
}
