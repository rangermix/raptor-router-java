package io.rangermix.routing.enums;

public enum Possibility {
    UNKNOWN, POSSIBLE, IMPOSSIBLE;

    private static final Possibility[] _values = values();

    public static Possibility valueOf(int id) {
        return _values[id];
    }
}
