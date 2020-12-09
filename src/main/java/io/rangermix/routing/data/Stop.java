package io.rangermix.routing.data;

import io.rangermix.routing.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Stop {
    @NotNull Agency agency;
    @NotNull String id;
    Coordinate coordinate;
    LocationType locationType;
    Stop parentStation;
}
