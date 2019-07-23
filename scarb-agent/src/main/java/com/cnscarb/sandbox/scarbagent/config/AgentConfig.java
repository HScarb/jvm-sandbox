package com.cnscarb.sandbox.scarbagent.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:agent.properties"})
public interface AgentConfig extends Config {
    @Key("kafka.bootstrap.servers")
    String kafkaBootstrapServers();

    @Key("kafka.topic.agent")
    String kafkaTopicAgent();

    @Key("kafka.topic.traffic")
    String kafkaTopicTraffic();

    @Key("app.token")
    String appToken();
}
