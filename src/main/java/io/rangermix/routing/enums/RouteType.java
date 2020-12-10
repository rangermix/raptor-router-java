package io.rangermix.routing.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RouteType {
    TRAM(0),
    SUBWAY(1),
    RAIL(2),
    BUS(3),
    FERRY(4),
    CABLE_TRAM(5),
    AERIAL_LIFT(6),
    FUNICULAR(7),
    TROLLEYBUS(11),
    MONORAIL(12),
    EXTENSION(100);

    private int id;
    private static final Map<Integer, RouteType> _values;

    static {
        _values = Arrays.stream(values()).collect(Collectors.toMap(RouteType::getId, Function.identity()));
    }

    RouteType(int id) {
        this.id = id;
    }

    public static RouteType valueOf(int id) {
        var type = _values.get(id);
        return type==null ? EXTENSION : type;
    }

    public int getId() {
        return id;
    }
}
