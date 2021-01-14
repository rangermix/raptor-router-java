package io.rangermix.raptorrouter.routing.enums;

public enum Direction {
    OUTBOUND, INBOUND;

    private static final Direction[] _values = values();

    public static Direction valueOf(int id) {
        return _values[id];
    }
}
