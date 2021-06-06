package com.immomio.osb.specification.response.service;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ServiceSpec {
    protected final String id;
    protected final String name;
    protected final String description;
    protected final boolean bindable;
    protected final List<ServicePlanSpec> plans;

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
