package com.cnscarb.sandbox.scarbagent.kafkareporter;

import com.cnscarb.sandbox.scarbagent.agent.intereact.kafkareporter.KafkaReporter;
import com.cnscarb.sandbox.scarbagent.config.AgentConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.internals.FutureRecordMetadata;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TestKafkaReporter {

    @Test
    public void testKafkaAgentReporter() throws ExecutionException, InterruptedException {
        KafkaProducer kafkaAgentReporter = KafkaReporter.getKafkaProducerAgent();
        ProducerRecord record = new ProducerRecord("agent_message", "value");
        Future<FutureRecordMetadata> future = kafkaAgentReporter.send(record);
        System.out.println(future.get());

        kafkaAgentReporter.close();
    }
}
