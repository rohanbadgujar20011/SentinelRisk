package com.sentinelrisk.common.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class ErrorResponse {

    private final boolean success;

    private final String errorCode;

    private final String message;

    private final String path;

    private final Instant timestamp;

    private final String traceId;
}
