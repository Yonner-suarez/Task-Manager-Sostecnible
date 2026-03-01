package com.sostecnible.TaskManager.infraestructure.controller.error;

public record ErrorResponse(
    int status,
    String message,
    long timestamp
) {}
