package com.sentinelrisk.common.exception;

import com.sentinelrisk.common.enums.ErrorCode;

public class ValidationException extends BaseException{
    protected ValidationException(ErrorCode errorCode) {
        super(ErrorCode.VALIDATION_ERROR);
    }

    protected ValidationException( String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}
