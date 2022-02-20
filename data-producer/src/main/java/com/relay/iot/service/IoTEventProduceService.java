package com.relay.iot.service;

import com.relay.iot.model.dto.IoTEventRequest;

public interface IoTEventProduceService {
    String publish(IoTEventRequest request);
}
