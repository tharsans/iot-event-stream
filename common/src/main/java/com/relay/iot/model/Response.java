package com.relay.iot.model;

import lombok.Data;

@Data
public class Response {
    private Integer code;
    private String message;
    private Object data;

    public Response() {
    }


    public Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response success(Object data)
    {
        return new Response(0, "success", data);
    }
}
