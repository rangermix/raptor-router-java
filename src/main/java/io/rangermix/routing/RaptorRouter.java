package io.rangermix.routing;

import io.rangermix.routing.model.DataPackage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RaptorRouter {
    private final DataPackage dataPackage;

    public Itinerary route(Stop from, Stop to) {
        return null;
    }
}
