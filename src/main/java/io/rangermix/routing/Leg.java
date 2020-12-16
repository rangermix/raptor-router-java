package io.rangermix.routing;

import java.time.Instant;

import io.rangermix.routing.model.Stop;

public class Leg {
    Stop from;
    Stop to;
    Instant startTime;
    Instant endTime;
}
