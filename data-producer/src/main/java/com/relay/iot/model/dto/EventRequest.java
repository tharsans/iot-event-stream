package com.relay.iot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRequest {
    private Integer heartBeat;
    private Integer total;
    private String type;
    private Long clusterId;
}
