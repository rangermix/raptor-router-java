package io.rangermix.routing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.StringJoiner;

@Data
@AllArgsConstructor
public class StopTime implements Serializable {
    @Serial
    private static final long serialVersionUID = 6922301736941245674L;
    @NotNull Trip trip;
    int arrivalTime;
    int departureTime;
    @NotNull Stop stop;
    int stopSequence;

    @Override
    public String toString() {
        return new StringJoiner(", ", StopTime.class.getSimpleName() + "[", "]")
                .add("trip=" + trip.getId())
                .add("arrivalTime=" + arrivalTime)
                .add("departureTime=" + departureTime)
                .add("stop=" + stop)
                .add("stopSequence=" + stopSequence)
                .toString();
    }
}