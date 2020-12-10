package io.rangermix.routing.data;

import io.rangermix.routing.enums.LocationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class Stop implements Serializable {
    @Serial
    private static final long serialVersionUID = 1212227387820663935L;
    @NotNull Agency agency;
    @NotNull String id;
    Coordinate coordinate;
    LocationType locationType;
    Stop parentStation;
}
