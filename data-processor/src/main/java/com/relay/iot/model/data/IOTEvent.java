package com.relay.iot.model.data;

/*import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;*/

import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;

import java.time.OffsetDateTime;

@Measurement(name = "iot-event")
public class IOTEvent {
    @Column(name = "sensor-id")
    private Long sensorId;

    @Column(name = "sensor_name")
    private String sensorName;

    @Column(name = "sensor_type")
    private String sensorType;

    @Column(name = "cluster_id")
    private Long clusterId;

    @Column(name = "time")
    private OffsetDateTime time;

    @Column(name = "value")
    private Double value;
}
