package io.rangermix.routing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@AllArgsConstructor
public class Trip {
    @NotNull Route route;
    @NotNull Service service;
    @NotNull String id;
    List<StopTime> stopTimes;
}
