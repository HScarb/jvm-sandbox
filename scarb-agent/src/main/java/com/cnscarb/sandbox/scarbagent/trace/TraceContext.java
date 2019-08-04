package com.cnscarb.sandbox.scarbagent.trace;

import com.alibaba.ttl.TransmittableThreadLocal;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class TraceContext {
    public static final Logger LOGGER = LoggerFactory.getLogger(TraceContext.class);
    private static TransmittableThreadLocal<Span> threadLocal = new TransmittableThreadLocal<Span>();

    public static TraceContext newInstance() {
        synchronized (TraceContext.class) {
            return new TraceContext();
        }
    }

    public Span startRootSpan() {
        return startRootSpan(null);
    }

    public Span startRootSpan(Span span) {
        if (span == null) {
            span = new Span();
        }
        String traceId = genTraceId();
        span.setTraceId(traceId);
        span.setSpanId(traceId);
        span.setParentSpanId(null);
        span.setStartTime(getMicTime());
        threadLocal.set(span);
        return span;
    }

    public <T extends Span> T startChildSpan(T child) {
        Span parent = threadLocal.get();
        child.setTraceId(parent.getTraceId());
        child.setSpanId(genSpanId());
        child.setParentSpanId(parent.getSpanId());
        child.setParentSpanName(parent.getName());
        child.setParent(parent);
        child.setStartTime(getMicTime());
        threadLocal.set(child);
        return child;
    }

    public <T extends Span> T startSpan(T span) {
        if (threadLocal.get() == null)
            return (T) startRootSpan();
        else
            return (T) startChildSpan(span);
    }

    public Span finishSpan() {
        Span currentSpan = threadLocal.get();
        if (currentSpan != null) {
            currentSpan.setEndTime(getMicTime());
            if (currentSpan.getParent() != null) {
                threadLocal.set(currentSpan.getParent());
                currentSpan = currentSpan.getParent();
            } else {
                threadLocal.set(null);
                currentSpan = null;
            }
        }
        return currentSpan;
    }

    public Span getCurrentSpan() {
        return threadLocal.get();
    }

    public void setCurrentSpan(Span currentSpan) {
        threadLocal.set(currentSpan);
    }

    public String genTraceId() {
        return UUID.randomUUID().toString();
    }

    public String genSpanId() {
        return UUID.randomUUID().toString();
    }

    public static Long getMicTime() {
        Long currentMillis = System.currentTimeMillis() * 1000;
        Long nanoTime = System.nanoTime();
        return currentMillis + (nanoTime - nanoTime) / 1000;
    }
}
