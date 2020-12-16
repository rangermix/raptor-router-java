package io.rangermix.routing;

import java.time.Instant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Stop {
    final io.rangermix.routing.model.Stop stop;
    long bestArrivalTime = Long.MAX_VALUE;
    long score = Long.MAX_VALUE;
    boolean marked = false;

}