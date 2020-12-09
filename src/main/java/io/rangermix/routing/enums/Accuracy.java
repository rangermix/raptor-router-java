package io.rangermix.routing.enums;

public enum Accuracy {
    APPROXIMATE, EXACT;

    private static final Accuracy[] _values = values();

    public static Accuracy valueOf(int id) {
        return _values[id];
    }
}
