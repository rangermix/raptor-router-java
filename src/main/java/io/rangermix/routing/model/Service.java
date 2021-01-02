package io.rangermix.routing.model;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.BitSet;

@Data
public class Service implements Serializable {
    @Serial
    private static final long serialVersionUID = -5149514752533129109L;
    public final String id;
    public final BitSet dayMask;
    public final ZonedDateTime startDate;
    public final ZonedDateTime endDate;
    public final int numDays;

    public Service(@NotNull String id, BitSet weekMask, ZonedDateTime startDate, ZonedDateTime endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;

        numDays = Period.between(startDate.toLocalDate(), endDate.toLocalDate()).getDays() + 1;
        dayMask = new BitSet(numDays);
        var day = startDate.getDayOfWeek().ordinal();
        for (int i = 0; i < numDays; i++) {
            dayMask.set(i, weekMask.get(day++));
            day %= 7;
        }
    }

    public boolean enabledOn(LocalDate date) {
        return dayMask.get(Period.between(startDate.toLocalDate(), date).getDays());
    }

    /**
     * Find the next enabled date from the given date (inclusive).
     *
     * @param date date to search from
     * @return next enabled date or null if no next enabled date
     */
    public ZonedDateTime nextEnabledDate(LocalDate date) {
        if (!hasDate(date))
            return null;
        var nextDateIndex = dayMask.nextSetBit(Period.between(startDate.toLocalDate(), date).getDays());
        return nextDateIndex == -1 ? null : startDate.plusDays(nextDateIndex);
    }

    public boolean hasDate(LocalDate date) {
        return startDate.toLocalDate().minusDays(1).isBefore(date) && endDate.toLocalDate().plusDays(1).isAfter(date);
    }
}
