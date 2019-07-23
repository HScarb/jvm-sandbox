package com.cnscarb.sandbox.scarbagent.agent.intereact.kafkareporter;

import com.cnscarb.sandbox.scarbagent.config.ConfigUtil;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class TokenKafkaInterceptor implements ProducerInterceptor {
    static private byte[] agentToken = ConfigUtil.getAgentConfig().appToken().getBytes();

    @Override
    public ProducerRecord onSend(ProducerRecord producerRecord) {
        producerRecord.headers().add("token", agentToken);
        ProducerRecord record = new ProducerRecord(
                producerRecord.topic(), producerRecord.partition(), producerRecord.timestamp(), producerRecord.key(),
                System.currentTimeMillis() + "," + producerRecord.value().toString());
        record.headers().add("token", agentToken);
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
