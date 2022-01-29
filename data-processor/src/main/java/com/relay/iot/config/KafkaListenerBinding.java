package com.relay.iot.config;

import com.relay.iot.model.dto.Event;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;

public interface KafkaListenerBinding {
    @Input("input-channel-1")
    KStream<String, Event> inputStream();
}
