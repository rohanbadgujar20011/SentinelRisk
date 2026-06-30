package com.sentinelrisk.common.exception;

import com.sentinelrisk.common.enums.ErrorCode;

public class BusinessException extends BaseException{

    protected BusinessException() {
        super(ErrorCode.BUSINESS_RULE_VIOLATION);
    }

    protected BusinessException( String message) {
        super(ErrorCode.BUSINESS_RULE_VIOLATION, message);
    }
}
