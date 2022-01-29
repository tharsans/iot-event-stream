package com.relay.iot.service.impl;

import com.relay.iot.model.dto.Event;
import com.relay.iot.model.dto.EventRequest;
import com.relay.iot.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Random;

@Service
public class EventServiceImpl implements EventService  {
    private final static String KAFKA_TOPIC = "iot-data";
    private final static Integer DFAULT_HEART_BEAT = 5;

    public EventServiceImpl(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private KafkaTemplate<String, Event> kafkaTemplate;

    public void publish(EventRequest request)
    {
        Random random = new Random();
        Double readingMin = 32.0;
        Double readingMax = 40.0;
        Integer minClusterId = 10;
        Integer maxClusterId = 1000;
        for(int i = 0; i < request.getTotal(); i++)
        {
            String id = Integer.toString(i + 1);
            Double reading = readingMin + (readingMax - readingMin) * random.nextDouble();
            Integer clusterId = minClusterId + random.nextInt(maxClusterId - minClusterId + 1);
            Event event = new Event(Long.valueOf(id), reading, 100L, "TEMP", "sensor-" + id, Long.valueOf(Integer.toString(clusterId)) );
            kafkaTemplate.send(KAFKA_TOPIC, event);
            Integer heartBeat = request.getHeartBeat();
            if(heartBeat == null || heartBeat <=0 )
            {
                heartBeat  = DFAULT_HEART_BEAT * 1000;
            }
            try {
                Thread.sleep(heartBeat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
