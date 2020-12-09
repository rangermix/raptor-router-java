package io.rangermix.routing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class StopTime {
    @NotNull Trip trip;
    int arrivalTime;
    int departureTime;
    @NotNull Stop stop;
    int stopSequence;
}
