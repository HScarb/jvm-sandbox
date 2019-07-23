package com.cnscarb.sandbox.scarbagent.agent.intereact.kafkareporter;

import com.cnscarb.sandbox.scarbagent.config.AgentConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

public class KafkaReporter {
    private static KafkaProducer<String, Object> kafkaProducerAgent = null;
    private static KafkaProducer<String, Object> kafkaProducerTraffic = null;

    public static KafkaProducer<String, Object> getKafkaProducerAgent() {
        AgentConfig agentConfig = ConfigFactory.create(AgentConfig.class);
        Properties props = new Properties();
        props.put("bootstrap.servers", agentConfig.kafkaBootstrapServers());
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", KafkaJsonSerializer.class.getName());
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, TokenKafkaInterceptor.class.getName());

        if (kafkaProducerAgent == null)
            kafkaProducerAgent = new KafkaProducer(props);
        return kafkaProducerAgent;
    }


}
