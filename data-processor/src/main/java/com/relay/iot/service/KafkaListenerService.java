package com.relay.iot.service;

//import com.relay.iot.config.KafkaListenerBinding;
import com.relay.iot.model.Field;
import com.relay.iot.model.dto.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class KafkaListenerService {

    @KafkaListener(topics = "iot-data",
            groupId = "id", containerFactory
            = "iotEventListener")
    public void process(Event event)
    {
        log.info("Received new Event: " + event);
       /* Map<String, Field> fields = new HashMap<>(){{
            Field field = new Field().type(Field.TypeEnum.)
           put("sensor_id", event.getId());
            put("sensor_name", event.getName());
            put("sensor_type", event.getType());
            put("cluster_id", event.getClusterId());
            put("value", event.getValue());
        }};*/
        List<Field> fields = new ArrayList<>()
        {{
           add(new Field("sensor_id", Field.FieldType.STRING, event.getId())) ;
            add(new Field("sensor_name", Field.FieldType.STRING, event.getName())) ;
            //add(new Field("sensor_type", Field.FieldType.STRING, event.getType())) ;
            add(new Field("cluster_id", Field.FieldType.LONG, event.getClusterId())) ;
            add(new Field("value", Field.FieldType.DOUBLE, event.getValue())) ;
        }};
        DataAccessService.save(event.getType(), fields);
    }
}
