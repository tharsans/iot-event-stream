package com.relay.iot.exception;

public class UnAuthorizedException extends EventStreamAppException {
    public UnAuthorizedException(Integer code, String message) {
        super(code, message);
    }

    public UnAuthorizedException(Integer code, String message, Exception exception) {
        super(code, message, exception);
    }
}
