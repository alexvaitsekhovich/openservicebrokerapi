package com.immomio.osb.specification.response.service;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ServiceSpec {
    protected String id;
    protected String name;
    protected String description;
    protected boolean bindable;
    protected List<ServicePlanSpec> plans;

    public ServiceSpec(String id, String name, String description, boolean bindable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bindable = bindable;
        this.plans = new ArrayList<>();
    }

    public ServiceSpec(String id, String name, String description) {
        this(id, name, description, true);
    }

    public void addPlan(ServicePlanSpec plan) {
        this.plans.add(plan);
    }
}
