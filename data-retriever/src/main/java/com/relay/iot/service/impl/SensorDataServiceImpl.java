package com.relay.iot.service.impl;

import com.relay.iot.model.Field;
import com.relay.iot.model.dto.SensorDataRequest;
import com.relay.iot.service.DataAccessService;
import com.relay.iot.service.SensorDataService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorDataServiceImpl implements SensorDataService {
    @Override
    public String calculateMeasurement(SensorDataRequest request) {
        validate(request);
        List<Field> filters = new ArrayList<>();
        if(request.getClusterId() != null)
        {
            filters.add(new Field("cluster_id", Field.FieldType.LONG, request.getClusterId()));
        }
        return DataAccessService.calculateMeasurementValue("iot-bucket", "median", request.getStartTime(), request.getEndTime(), request.getEventType(), filters );
    }

    protected boolean validate(SensorDataRequest request) {
        return true;
    }
}
