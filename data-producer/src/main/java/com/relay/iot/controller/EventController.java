package com.relay.iot.controller;

import com.relay.iot.model.dto.EventRequest;
import com.relay.iot.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class EventController {
    @Autowired
    private EventService eventService;

    @PostMapping(value = "producer/events", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String produce(@RequestBody EventRequest request) {
        eventService.publish(request);
        return "Domain crawler has scrapped your data";
    }
}
