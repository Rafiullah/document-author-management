package com.momand.docauthor.author.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthorProducerKafka {
    private static final String TOPIC = "author_events";
    private static final Logger log = LoggerFactory.getLogger(AuthorProducerKafka.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public AuthorProducerKafka(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAuthorEvent(String message){
        kafkaTemplate.send(TOPIC, message);
        log.info("Author Producer produced message-->{}", message);
    }

}
