package com.relay.iot.service.impl;

import com.relay.iot.model.Constant;
import com.relay.iot.model.dto.Event;
import com.relay.iot.model.dto.IoTEventRequest;
import com.relay.iot.service.IoTEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
@Slf4j
public class IoTEventServiceImpl implements IoTEventService {
    @Value("${data.producer.config.stream.topic}")
    private String streamTopic;

    private static final Integer DEFAULT_HEART_BEAT = 5; //5 seconds

    private static final Integer MIN_CLUSTER_ID = 100;
    private static final Integer MAX_CLUSTER_ID = 105;
    private static final Double MIN_TEMPERATURE = -10.0;
    private static final Double MAX_TEMPERATURE = 40.0;
    private static final Double MIN_HUMIDITY = 30.0;
    private static final Double MAX_HUMIDITY = 80.0;
    private static final Double MIN_PRESSURE = 100000.0;
    private static final Double MAX_PRESSURE = 150000.0;

    private KafkaTemplate<String, Event> kafkaTemplate;

    public IoTEventServiceImpl(KafkaTemplate<String, Event> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String publish(IoTEventRequest request)
    {
        Integer messages = 0;
        for(int i = 0; i < request.getTotal(); i++)
        {
            Event event = getEvent(i, request.getType());
            kafkaTemplate.send(streamTopic, event);
            log.info("New event has been published successfully", event);
            Integer heartBeat = request.getHeartBeat();
            if(heartBeat == null || heartBeat <=0 )
            {
                heartBeat  = DEFAULT_HEART_BEAT * 1000;
            }
            try {
                Thread.sleep(heartBeat);
                messages++;
            } catch (InterruptedException e) {
                log.error("There are errors occurred while waiting", e);
            }
        }
        return messages + " messages have been published successfully";
    }

    protected Event getEvent(Integer id, String sensorType)
    {
        return new Event(getSensorId(id), getSensorValue(sensorType), getTime(), sensorType, getSensorName(id), getClusterId() );
    }

    protected Long getSensorId(Integer id)
    {
        return Long.valueOf (Integer.toString(id + 1));
    }

    protected String getSensorName(Integer id)
    {
        return "Sensor #" + (id + 1);
    }

    protected Double getSensorValue(String sensorType)
    {
        Double value = 0.0;
        if(sensorType.equalsIgnoreCase(Constant.SENSOR_TYPE.TEMPERATURE.toString())){
            value = getRoomTemperature();
        }
        else if(sensorType.equalsIgnoreCase(Constant.SENSOR_TYPE.HUMIDITY.toString())){
            value = getRoomHumidity();
        }
        else if(sensorType.equalsIgnoreCase(Constant.SENSOR_TYPE.PRESSURE.toString())){
            value = getRoomPressure();
        }
        return value;
    }

    protected Double getRoomTemperature()
    {
        Random random = new Random();
        return MIN_TEMPERATURE + (MAX_TEMPERATURE - MIN_TEMPERATURE) * random.nextDouble();
    }

    protected Double getRoomHumidity()
    {
        Random random = new Random();
        return MIN_HUMIDITY + (MAX_HUMIDITY - MIN_HUMIDITY) * random.nextDouble();
    }

    protected Double getRoomPressure()
    {
        Random random = new Random();
        return MIN_PRESSURE + (MAX_PRESSURE - MIN_PRESSURE) * random.nextDouble();
    }

    protected Long getClusterId()
    {
        Random random = new Random();
        return Long.valueOf(Integer.toString(MIN_CLUSTER_ID + random.nextInt(MAX_CLUSTER_ID - MIN_CLUSTER_ID + 1)));
    }

    protected Long getTime()
    {
        return Instant.now().toEpochMilli();
    }
}
