package io.rangermix.raptorrouter.routing.enums;

public enum LocationType {
    STOP, STATION, ENTRANCE_EXIT, GENERIC_NODE, BOARDING_AREA;

    private static final LocationType[] _values = values();

    public static LocationType valueOf(int id) {
        return _values[id];
    }
}
