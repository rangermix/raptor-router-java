package io.rangermix.routing.data;

import io.rangermix.routing.enums.Accuracy;
import io.rangermix.routing.enums.BoardingInstruction;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;

@Data
public class StopTimes {
    @NotNull String tripId;
    LocalTime arrivalTime;
    LocalTime departureTime;
    @NotNull String stopId;
    int stopSequence;
    String headsign;
    BoardingInstruction pickupType = BoardingInstruction.AVAILABLE;
    BoardingInstruction dropOffType = BoardingInstruction.AVAILABLE;
    BoardingInstruction continuousPickup = BoardingInstruction.NOT_AVAILABLE;
    BoardingInstruction continuousDropOff = BoardingInstruction.NOT_AVAILABLE;
    float shapeDistanceTraveled;
    Accuracy timepoint = Accuracy.EXACT;
}
