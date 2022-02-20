package com.relay.iot.service;

//import com.relay.iot.config.KafkaListenerBinding;

import com.relay.iot.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaListenerService {
    private static final String CONSUME_TOPIC = "iot-data";
    private static final String GROUP_ID = "id";

    @KafkaListener(topics = CONSUME_TOPIC, groupId = GROUP_ID, containerFactory
            = "iotEventListener")
    public void process(@Payload List<Event> events,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets)
    {
        //log.info("Received new Event: " + event);
        //DataAccessService.save(event.getType(), getFields(event));
        //log.info("Successfully saved that event: " + event);
        for (int i = 0; i < events.size(); i++) {
            log.info("Received message='{}' with partition-offset='{}'", events.get(i),
                    partitions.get(i) + "-" + offsets.get(i));
        }
        DataAccessService.saveAsBatch( events);
        log.info("All batch messages received");

    }
}
