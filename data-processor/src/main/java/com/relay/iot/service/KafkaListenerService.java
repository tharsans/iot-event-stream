package com.relay.iot.service;

//import com.relay.iot.config.KafkaListenerBinding;
import com.relay.iot.model.dto.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
//@EnableBinding(KafkaListenerBinding.class)
public class KafkaListenerService {

    /*@StreamListener("input-channel-1")
    public void process(KStream<String, Event> input)
    {
        Double avg, min, max, median;
        input.foreach( (k,v) -> System.out.println("key: " + k + ", value: " + v));

    }*/

    @Autowired
    private DBService dbService;

    @KafkaListener(topics = "iot-data",
            groupId = "id", containerFactory
            = "iotEventListener")
    public void process(Event event)
    {
        System.out.println("New Entry: "
                + event);
        dbService.write(event);
    }
}
