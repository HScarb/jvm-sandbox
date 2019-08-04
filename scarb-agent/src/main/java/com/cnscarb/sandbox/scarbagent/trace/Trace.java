package com.cnscarb.sandbox.scarbagent.trace;

import com.cnscarb.sandbox.scarbagent.agent.intereact.traffic.TrafficReporter;
import com.cnscarb.sandbox.scarbagent.config.ConfigCheckUtil;

public class Trace {
    private TraceContext traceContext = TraceContext.newInstance();

    public TrafficSpan start(String name, String spanId, String traceId) {
        TrafficSpan span = null;
        if (traceId != null) {      // set upper spanId as parent spanId
            span = new TrafficSpan();
            span.setTraceId(traceId);
            span.setParentSpanId(spanId);
            span.setName(name);
            span.setSpanId(traceContext.genSpanId());
            span.setStartTime(TraceContext.getMicTime());
            traceContext.setCurrentSpan(span);
        } else {
            if (ConfigCheckUtil.checkIfMainEntrance()) {
                span = newSpan(name);
            } else {
                span = new TrafficSpan();
            }
        }
        span.setIsEntrance(true);
        return span;
    }

    public TrafficSpan enter(String name) {
        if (traceContext.getCurrentSpan() == null) {
            return new TrafficSpan();
        }
        return newSpan(name);
    }

    public void exit() {
        Span current = traceContext.getCurrentSpan();
        if (current != null) {
            traceContext.finishSpan();
            // TODO save traffic
        }
    }

    public TrafficSpan newSpan(String name) {
        TrafficSpan span = new TrafficSpan();
        span.setName(name);
        return traceContext.startSpan(span);
    }

    public void exitWithoutStoringSpan() {
        Span current = traceContext.getCurrentSpan();
        if (current != null) {
            traceContext.finishSpan();
        }
    }

    public void saveImmediately() {
        TrafficSpan currentSpan = (TrafficSpan) traceContext.getCurrentSpan();
        if (currentSpan != null) {
            TrafficReporter.saveSpan(currentSpan);
        }
    }

    public void saveImmediately(TrafficSpan trafficSpan) {
        TrafficReporter.saveSpan(trafficSpan);
    }

    public TrafficSpan getCurrentTrafficSpan() {
        TrafficSpan currentSpan = (TrafficSpan) traceContext.getCurrentSpan();
        if (currentSpan == null) {
            return new TrafficSpan();
        }
        return currentSpan;
    }
}
