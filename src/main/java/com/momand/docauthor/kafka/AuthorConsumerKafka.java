package com.momand.docauthor.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AuthorConsumerKafka {

    private static final Logger log = LoggerFactory.getLogger(AuthorConsumerKafka.class);

    @KafkaListener(topics = "author_events", groupId = "author-consumer-group")
    public void consume(ConsumerRecord<String, String> record){
        String message = record.value();
        log.info("Author Consumer consumed message-->{}", message);
    }
}
