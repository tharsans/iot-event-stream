package com.relay.iot.exception;

import lombok.Data;

@Data
public class EventStreamAppException extends RuntimeException {
    private Integer code;
    private String message;
    private Exception exception;

    public EventStreamAppException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public EventStreamAppException(Integer code, String message, Exception exception) {
        super(message, exception);
        this.code = code;
        this.message = message;
        this.exception = exception;
    }
}
