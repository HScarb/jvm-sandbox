package com.cnscarb.sandbox.scarbagent.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigUtil {
    static private AgentConfig agentConfig = null;

    static public AgentConfig getAgentConfig() {
        if (agentConfig == null) {
            agentConfig = ConfigFactory.create(AgentConfig.class);
        }
        return agentConfig;
    }

    static public void reloadAgentConfig() {
        agentConfig = ConfigFactory.create(AgentConfig.class);
    }
}
