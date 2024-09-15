package com.momand.docauthor.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DocumentConsumerKafka {

    private static final Logger log = LoggerFactory.getLogger(DocumentConsumerKafka.class);

    @KafkaListener(topics = "document_events", groupId = "document-consumer-group")
    public void consume(ConsumerRecord<String, String> record){
        String message = record.value();
        log.info("Document Consumer consumed message-->{}", message);
    }
}
