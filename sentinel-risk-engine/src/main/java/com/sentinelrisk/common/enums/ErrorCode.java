package com.sentinelrisk.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("SR-500", "Internal server error"),

    VALIDATION_ERROR("SR-400", "Validation failed"),

    RESOURCE_NOT_FOUND("SR-404", "Resource not found"),

    BUSINESS_RULE_VIOLATION("SR-422", "Business rule violated"),

    UNAUTHORIZED("SR-401", "Unauthorized"),

    FORBIDDEN("SR-403", "Forbidden");

    private final String code;

    private final String message;

}
