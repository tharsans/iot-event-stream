package com.relay.iot.controller;

import com.relay.iot.model.Response;
import com.relay.iot.model.dto.AggDataRequest;
import com.relay.iot.service.AggDataService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sensor")
public class AggDataController {

    private AggDataService aggDataService;

    public AggDataController(AggDataService aggDataService)
    {
        this.aggDataService = aggDataService;
    }

    @PostMapping("/aggVal")
    public Response getAggregatedValue(@RequestBody AggDataRequest request)
    {
        return Response.success(aggDataService.calculateAggVal(request));
    }
}
