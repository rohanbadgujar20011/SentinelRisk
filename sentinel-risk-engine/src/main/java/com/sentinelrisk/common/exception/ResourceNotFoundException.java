package com.sentinelrisk.common.exception;

import com.sentinelrisk.common.enums.ErrorCode;

public class ResourceNotFoundException extends  BaseException{

    protected ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }

    protected ResourceNotFoundException( String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
