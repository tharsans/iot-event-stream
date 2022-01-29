package com.relay.iot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AggDataRequest {
    private String startTime;
    private String endTime;
    private String operation;
    private String eventType;
    private Long clusterId;
}
