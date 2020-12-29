package io.rangermix.routing;

import io.rangermix.routing.model.DataPackage;
import io.rangermix.routing.model.Stop;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Query {
    final DataPackage dataPackage;
    final Stop from;
    final Stop to;
    final long startTime;
}
