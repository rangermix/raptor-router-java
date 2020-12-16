package io.rangermix.routing.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.util.BitSet;
import java.util.Calendar;

@Data
public class Service implements Serializable {
    @Serial
    private static final long serialVersionUID = -5149514752533129109L;
    @NotNull String id;
    BitSet dayMask;
    Calendar startDate;
    Calendar endDate;
    int numDays;

    public Service(@NotNull String id, BitSet weekMask, Calendar startDate, Calendar endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;

        numDays = (int) Duration.between(startDate.toInstant(), endDate.toInstant()).toDays() + 1;
        dayMask = new BitSet(numDays);
        var day = startDate.get(Calendar.DAY_OF_WEEK);
        for (int i = 0; i < numDays; i++) {
            dayMask.set(i, weekMask.get(day++));
            day %= 7;
        }
    }
}