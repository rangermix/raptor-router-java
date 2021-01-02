package io.rangermix.routing;

import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class Stop {
    final io.rangermix.routing.model.Stop stop;
    long bestArrivalTime = Long.MAX_VALUE;
    long lastArrivalTime = Long.MAX_VALUE;
    long score = Long.MAX_VALUE;
    boolean marked = false;
    Stop comeFromStop = null;
    Object transfer = null;

    void addTransfer(Set<Stop> markedStops, Stop p, Object transfer, long arrivalTime) {
        lastArrivalTime = bestArrivalTime;
        bestArrivalTime = arrivalTime;
        comeFromStop = p;
        this.transfer = transfer;
        marked = true;
        markedStops.add(this);
    }
}
