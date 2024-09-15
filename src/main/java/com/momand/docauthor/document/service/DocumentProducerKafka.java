package com.momand.docauthor.document.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DocumentProducerKafka {

    private static final String TOPIC = "document_events";
    private static final Logger log = LoggerFactory.getLogger(DocumentProducerKafka.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    public DocumentProducerKafka(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDocumentEvent(String message){
        kafkaTemplate.send(TOPIC, message);
        log.info("Document Producer produced message-->{}", message);
    }
}
