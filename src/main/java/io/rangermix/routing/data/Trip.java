package io.rangermix.routing.data;

import io.rangermix.routing.enums.Direction;
import io.rangermix.routing.enums.Possibility;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class Trip {
    @NotNull String routeId;
    @NotNull String serviceId;
    @NotNull String id;
    String headsign;
    String shortName;
    Direction directionId;
    String blockId;
    String shapeId;
    Possibility wheelchairAccessible = Possibility.UNKNOWN;
    Possibility bikesAllowed = Possibility.UNKNOWN;
}
