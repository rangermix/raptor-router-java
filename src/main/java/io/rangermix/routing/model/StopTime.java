package io.rangermix.routing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@AllArgsConstructor
public class StopTime implements Serializable {
    @Serial
    private static final long serialVersionUID = 6922301736941245674L;
    final Trip trip;
    final long arrivalTime;
    final long departureTime;
    @Getter
    final Stop stop;
    final int stopSequence;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        StopTime stopTime = (StopTime) o;
        return stopSequence == stopTime.stopSequence && trip.equals(stopTime.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trip, stopSequence);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StopTime.class.getSimpleName() + "[", "]").add("trip=" + trip.getId())
                .add("arrivalTime=" + arrivalTime)
                .add("departureTime=" + departureTime)
                .add("stop=" + stop)
                .add("stopSequence=" + stopSequence)
                .toString();
    }
}