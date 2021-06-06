package com.immomio.osb.specification.response.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DeleteOperation {
    public static final String DELETE_OPERATION_PLACEHOLDER = "task_10";

    private final String operation;
}
