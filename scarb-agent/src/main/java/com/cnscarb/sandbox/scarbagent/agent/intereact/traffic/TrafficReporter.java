package com.cnscarb.sandbox.scarbagent.agent.intereact.traffic;

import com.cnscarb.sandbox.scarbagent.trace.Span;

public class TrafficReporter {
    public static void saveSpan(Span span) {
        System.out.println(span);
    }
}
