package org.sesac.wagekeeper.global.error.exception;


import org.sesac.wagekeeper.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}