package com.relay.iot.service;

import com.relay.iot.model.dto.SensorDataRequest;

public interface SensorDataService {
    public Double calculateValue(SensorDataRequest request);
}
