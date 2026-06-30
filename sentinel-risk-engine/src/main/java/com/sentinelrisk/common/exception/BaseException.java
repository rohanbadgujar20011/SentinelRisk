package com.sentinelrisk.common.exception;

import com.sentinelrisk.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException  extends  RuntimeException{

    private final ErrorCode errorCode;


    protected BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    protected BaseException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
