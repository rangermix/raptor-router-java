package io.rangermix.raptorrouter.routing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

@AllArgsConstructor
public class StopTime implements Serializable {
    @Serial
    private static final long serialVersionUID = 6922301736941245674L;
    public final Trip trip;
    public final long arrivalTime;
    public final long departureTime;
    @Getter
    public final Stop stop;
    public final int stopSequence;

    public long nextArrivalTimeFromDate(LocalDate tripStartDate) {
        var date = trip.service.nextEnabledDate(tripStartDate);
        return date == null ? Long.MAX_VALUE : date.plusSeconds(arrivalTime).toEpochSecond();
    }

    public long nextDepartureTimeFromDate(LocalDate tripStartDate) {
        var date = trip.service.nextEnabledDate(tripStartDate);
        return date == null ? Long.MAX_VALUE : date.plusSeconds(departureTime).toEpochSecond();
    }

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
        return new StringJoiner(", ", StopTime.class.getSimpleName() + "[", "]").add("trip=" + trip.id)
                .add("arrivalTime=" + arrivalTime)
                .add("departureTime=" + departureTime)
                .add("stop=" + stop.id)
                .add("stopSequence=" + stopSequence)
                .toString();
    }
}
