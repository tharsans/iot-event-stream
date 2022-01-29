package com.relay.iot.service;

import com.relay.iot.model.dto.AggDataRequest;

public interface AggDataService {
    String calculateAggVal(AggDataRequest request);
}
