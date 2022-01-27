package com.relay.iot.service;

import com.relay.iot.model.dto.EventRequest;

public interface EventService {
    public void publish(EventRequest request);
}
