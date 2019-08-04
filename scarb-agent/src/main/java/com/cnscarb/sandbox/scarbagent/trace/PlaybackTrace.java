package com.cnscarb.sandbox.scarbagent.trace;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.cnscarb.sandbox.scarbagent.agent.intereact.traffic.TrafficFetcher;
import com.cnscarb.sandbox.scarbagent.config.ConfigCheckUtil;

public class PlaybackTrace extends Trace {
    private InheritableThreadLocal<TrafficSpan> originCurrentSpan = new TransmittableThreadLocal<TrafficSpan>();
    private static TrafficFetcher trafficFetcher = new TrafficFetcher();

    public TrafficSpan start(String name, String spanId, String traceid, String originSpanId, String originTraceId) {
        TrafficSpan originSpan = null;
        if (originSpanId != null) {
            if (ConfigCheckUtil.checkIfMainEntrance()) {
                originSpan = trafficFetcher.fetch(originSpanId);
            } else {
                originSpan = trafficFetcher.fetchByParent(originSpanId);
            }
        }
        if (originSpan == null) {
            originSpan = new TrafficSpan();
            originSpan.setTraceId(originTraceId);
        }
        originCurrentSpan.set(originSpan);

        TrafficSpan currentSpan = super.start(name, spanId, traceid);
        currentSpan.setOriginTraceId(originSpan.getTraceId());
        return currentSpan;
    }

    @Override
    public TrafficSpan enter(String name) {
        TrafficSpan now = originCurrentSpan.get();
        TrafficSpan child;
        if (now == null) {
            now = new TrafficSpan();
        }
        if (now.getSpanId() == null) {
            child = new TrafficSpan();
            child.setTraceId(now.getTraceId());
        } else {
            String childSpanId = now.pollChild(name);
            if (childSpanId == null) {
                child = new TrafficSpan();
                child.setTraceId(now.getTraceId());
            } else {
                child = trafficFetcher.fetch(childSpanId);
            }
        }
        child.setParentSpan(now);
        originCurrentSpan.set(child);
        TrafficSpan currentSpan = super.enter(name);
        currentSpan.setOriginTraceId(child.getTraceId());
        return currentSpan;
    }

    @Override
    public void exit() {
        originCurrentSpan.set(originCurrentSpan.get().getParentSpan());
        super.exit();
    }

    public void exitWithoutStoringSpan() {
        originCurrentSpan.set(originCurrentSpan.get().getParentSpan());
        super.exitWithoutStoringSpan();
    }

    public TrafficSpan getOriginTrafficSpan() {
        TrafficSpan span = originCurrentSpan.get();
        return span;
    }
}
