package com.relay.iot.exception;

public class InvalidDataException extends EventStreamAppException {
    public InvalidDataException(Integer code, String message) {
        super(code, message);
    }

    public InvalidDataException(Integer code, String message, Exception exception) {
        super(code, message, exception);
    }
}
