package com.relay.iot.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IoTEventRequest {
    private Integer heartBeat;
    private Integer total;
    private String type;
    private Long clusterId;
}
