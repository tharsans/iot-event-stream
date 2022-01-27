package com.relay.iot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDataRequest {
    private OffsetDateTime from;
    private OffsetDateTime to;
    private String eventType;
    private Long clusterId;
}
