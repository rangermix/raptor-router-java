package io.rangermix.routing.data;

import io.rangermix.routing.enums.LocationType;
import io.rangermix.routing.enums.Possibility;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.TimeZone;

@Data
public class Stop {
    @NotNull Agency agency;
    @NotNull String id;
    String code;
    String name;
    String description;
    Coordinate coordinate;
    String zoneId;
    String url;
    LocationType locationType = LocationType.STOP;
    String parentStationId;
    TimeZone timeZone;
    Possibility wheelchairBoarding = Possibility.UNKNOWN;
    String levelId;
    String platformCode;
}
