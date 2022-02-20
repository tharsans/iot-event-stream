/*
package com.relay.iot.service;

import com.relay.iot.ProcessorApp;
import com.relay.iot.model.Constant;
import com.relay.iot.model.Event;
import com.relay.iot.model.dto.IoTEventRequest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProcessorApp.class)
@DirtiesContext
@EmbeddedKafka(topics = {"TEST_TOPIC"})
class KafkaListenerServiceTest {
    private static final String TEST_TOPIC_NAME = "TEST_TOPIC";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private IoTEventProduceService produceService;

    private IoTEventRequest buildEventRequest(Integer total, String type) {
        return IoTEventRequest.builder().total(total).type(type).build();
    }

    */
/**
     * We verify the output in the topic. With an simulated consumer.
     *//*

    @Test
    public void itShould_ProduceCorrectExampleDTO_to_TOPIC_EXAMPLE_EXTERNE() {
        // GIVEN
        IoTEventRequest eventRequest = buildEventRequest(3, Constant.SENSOR_TYPE.TEMPERATURE.toString());
        // simulation consumer
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group_consumer_test", "false", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConsumerFactory consumerFactory = new DefaultKafkaConsumerFactory<String, Event>(consumerProps, new StringDeserializer(), new JsonDeserializer<>(Event.class, false));
        Consumer<String, Event> consumerServiceTest = consumerFactory.createConsumer();

        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumerServiceTest, TEST_TOPIC_NAME);
        // WHEN
        produceService.publish(eventRequest);
        // THEN

        ConsumerRecord<String, Event> receivedEventRecord = KafkaTestUtils.getSingleRecord(consumerServiceTest, TEST_TOPIC_NAME);
        Event receivedEvent = receivedEventRecord.value();

        assertEquals("Une description", receivedEvent.getName());

        consumerServiceTest.close();
    }
}
*/

