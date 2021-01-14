package io.rangermix.raptorrouter.util;

import org.slf4j.Logger;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class StopWatch {
    private final Logger log;
    private Instant startTime;
    private Instant lastLap;
    private List<Instant> lapHistory;

    public StopWatch(Logger log) {
        this.log = log;
        reset();
    }

    public void reset() {
        lapHistory = new ArrayList<>();
        startTime = Instant.now();
        lastLap = startTime;
    }

    public void start(String string, Object... objects) {
        reset();
        writeLog(string, Duration.ZERO, objects);
    }

    public void lap(String string, Object... objects) {
        var now = Instant.now();
        var lapTime = Duration.between(lastLap, now);
        lapHistory.add(lastLap);
        lastLap = now;
        writeLog(string, lapTime, objects);
    }

    private void writeLog(String string, Duration lapTime, Object[] objects) {
        log.info("{} - " + string, lapTime, objects);
    }
}
