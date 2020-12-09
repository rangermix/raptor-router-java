package io.rangermix.routing.data;

import lombok.Data;

@Data
public class Coordinate {
    private static final double RADIUS = 6371000;
    private static final double RADIANS = 3.14159 / 180;
    double lat;
    double lng;

    public Coordinate(double lat, double lng) {
        assert Math.abs(lat) <= 90 && Math.abs(lng) <= 180;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Distance to another earth coordinate in meter.
     * see <a href="https://www.movable-type.co.uk/scripts/latlong.html#equirectangular">Equirectangular approximation</a>
     *
     * @param other coordinate to compute distance
     * @return distance in meter
     */
    public double distanceTo(Coordinate other) {
        double x = (lng - other.lng) * Math.cos((lat + other.lat) * RADIANS / 2);
        double y = (lat - other.lat);
        return Math.sqrt(x * x + y * y) * RADIUS * RADIANS;
    }

}
