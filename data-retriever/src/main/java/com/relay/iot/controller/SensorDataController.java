package com.relay.iot.controller;

import com.relay.iot.model.dto.Response;
import com.relay.iot.model.dto.SensorDataRequest;
import com.relay.iot.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensor")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @PostMapping("/aggData")
    public Response getAggregatedValue(@RequestBody SensorDataRequest request)
    {
        return Response.success(sensorDataService.calculateAggVal(request));
    }
}
