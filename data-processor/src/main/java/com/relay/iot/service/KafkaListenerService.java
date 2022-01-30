package com.relay.iot.service;

//import com.relay.iot.config.KafkaListenerBinding;

import com.relay.iot.model.Field;
import com.relay.iot.model.dto.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KafkaListenerService {
    private static final String CONSUME_TOPIC = "iot-data";
    private static final String GROUP_ID = "id";

    @KafkaListener(topics = CONSUME_TOPIC, groupId = GROUP_ID, containerFactory
            = "iotEventListener")
    public void process(Event event)
    {
        log.info("Received new Event: " + event);
        DataAccessService.save(event.getType(), getFields(event));
        log.info("Successfully saved that event: " + event);
    }

    protected List<Field> getFields(Event event)
    {
        return new ArrayList<>()
        {{
            add(new Field("sensor_id", Field.FieldType.STRING, event.getId())) ;
            add(new Field("sensor_name", Field.FieldType.STRING, event.getName())) ;
            add(new Field("cluster_id", Field.FieldType.LONG, event.getClusterId())) ;
            add(new Field("value", Field.FieldType.DOUBLE, event.getValue())) ;
        }};
    }
}
