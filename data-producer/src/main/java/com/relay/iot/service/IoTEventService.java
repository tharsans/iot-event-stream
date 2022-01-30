package com.relay.iot.service;

import com.relay.iot.model.dto.IoTEventRequest;

public interface IoTEventService {
    String publish(IoTEventRequest request);
}
