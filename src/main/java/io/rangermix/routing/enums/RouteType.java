package io.rangermix.routing.enums;

public enum RouteType {
    TRAM, SUBWAY, RAIL, BUS, FERRY, CABLE_TRAM, AERIAL_LIFT, FUNICULAR, TROLLEYBUS, MONORAIL;

    private static final RouteType[] _values = values();

    public static RouteType valueOf(int id) {
        return _values[id];
    }
}
