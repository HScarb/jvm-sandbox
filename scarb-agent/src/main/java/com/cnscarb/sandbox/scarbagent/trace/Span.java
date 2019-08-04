package com.cnscarb.sandbox.scarbagent.trace;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Span {
    private String traceId;
    private String spanId;
    private String parentSpanId;
    private String parentSpanName;
    private String name;
    private Long startTime;
    private long endTime;
    private String component;   // component type
    private Span parent;
}
