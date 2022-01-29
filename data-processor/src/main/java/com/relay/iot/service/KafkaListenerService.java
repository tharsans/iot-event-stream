package com.relay.iot.service;

import com.relay.iot.config.KafkaListenerBinding;
import com.relay.iot.model.dto.Event;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(KafkaListenerBinding.class)
public class KafkaListenerService {
    @StreamListener("input-channel-1")
    public void process(KStream<String, Event> input)
    {
        Double avg, min, max, median;
        input.foreach( (k,v) -> System.out.println("key: " + k + ", value: " + v));

    }
}
