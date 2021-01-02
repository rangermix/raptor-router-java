package io.rangermix.routing;

import java.util.StringJoiner;

public class Leg {
    public final io.rangermix.routing.model.Stop from;
    public final io.rangermix.routing.model.Stop to;
    public final long startTime;
    public final long endTime;
    public final Object transfer;

    public Leg(Stop to) {
        var from = to.comeFromStop;
        var fromStop = from.stop;
        var toStop = to.stop;
        this.from = fromStop;
        this.to = toStop;
        startTime = from.bestArrivalTime;
        endTime = to.bestArrivalTime;
        transfer = to.transfer;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Leg.class.getSimpleName() + "[", "]").add("from=" + from)
                .add("to=" + to)
                .add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .add("transfer=" + transfer)
                .toString();
    }
}
