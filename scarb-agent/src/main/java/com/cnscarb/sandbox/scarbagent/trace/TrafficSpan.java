package com.cnscarb.sandbox.scarbagent.trace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Queue;

@Setter
@Getter
@JsonIgnoreProperties({"parent", "parentSpanName"})
public class TrafficSpan extends Span {
    private static ObjectMapper objectMapper = new ObjectMapper();

    @JsonIgnore
    private TrafficSpan parentSpan;

    private Object mockData;
    private Object checkData;
    private Object extraData;

    private Boolean isEntrance = false;     // micro service entrance
    private String originTraceId;

    private Map<String, Queue<String>> children;

    @JsonIgnore
    private Object attachment;

    private Integer recordTaskExecuteId;
    private Integer playbackTaskExecuteId;
    private Integer trafficEntranceId;
    private Integer machineId;

    public String pollChild(String name) {
        if (children != null && children.containsKey(name)) {
            return children.get(name).poll();
        }
        return null;
    }

    public <T> T getMockData(Class<T> clazz) {
        return objectMapper.convertValue(getMockData(), clazz);
    }

    public <T> T getAttachment(Class<T> clazz) {
        return (T) attachment;
    }
}
