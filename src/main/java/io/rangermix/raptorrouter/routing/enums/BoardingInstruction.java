package io.rangermix.raptorrouter.routing.enums;

public enum BoardingInstruction {
    AVAILABLE, NOT_AVAILABLE, PHONE_AGENCY, COORDINATE_WITH_DRIVER;

    private static final BoardingInstruction[] _values = values();

    public static BoardingInstruction valueOf(int id) {
        return _values[id];
    }
}
