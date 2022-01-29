package com.relay.iot.service;

import com.relay.iot.model.dto.SensorDataRequest;
import com.relay.iot.model.dto.SensorDataResponse;

public interface SensorDataService {
    public String calculateAggVal(SensorDataRequest request);
}
