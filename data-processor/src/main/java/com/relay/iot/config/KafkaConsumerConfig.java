package com.relay.iot.config;

import com.relay.iot.model.Event;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchLoggingErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    @Value("${data.processor.config.steam.url}")
    private String streamUrl;

    //@Value("${stream.batch.size}")
    //private String batchSize;

    //@Value("${stream.batch.one.element.min.bytes}")
    //private String elementSize;

    @Bean
    public ConsumerFactory<String, Event> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, streamUrl);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.relay.iot.model");
        //props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, getFetchMinBytes());
        //props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, getBatchSize());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Event> iotEventListener() {
        ConcurrentKafkaListenerContainerFactory<String, Event> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.setBatchListener(true);
        //factory.setBatchErrorHandler(new BatchLoggingErrorHandler());
        return factory;
    }

    /*protected String getBatchSize(){
        return batchSize;
    }

    protected String getFetchMinBytes(){
        return Integer.toString(Integer.parseInt(getBatchSize()) * Integer.parseInt(elementSize));
    }*/
}
