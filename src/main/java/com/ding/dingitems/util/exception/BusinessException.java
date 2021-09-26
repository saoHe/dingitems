package com.ding.dingitems.util.exception;

public class BusinessException extends Exception {
    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Exception e) {
        super(message,e);
    }

    public BusinessException(BusinessException e) {
        super(e);
    }

}
