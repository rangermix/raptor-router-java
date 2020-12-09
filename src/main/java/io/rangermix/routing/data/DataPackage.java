package io.rangermix.routing.data;

import org.onebusaway.gtfs.impl.GtfsDaoImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class DataPackage {
    private final GtfsDaoImpl staticGTFSSource;
    private Map<String, Agency> agencyMap;
    private Map<String, Stop> stopMap;
    private Map<String, Route> routeMap;
    private Map<String, Trip> tripMap;

    public DataPackage(GtfsDaoImpl staticGTFSSource) {
        this.staticGTFSSource = staticGTFSSource;
    }

    private Map<String, Agency> loadAgency(GtfsDaoImpl source) {
        Map<String, Agency> agencyMap = new HashMap<>();
        var sourceAgencies = source.getAllAgencies();

        return agencyMap;
    }

//    private Agency convertAgency(org.onebusaway.gtfs.model.Agency source) {
//        var agency = new Agency();
//        return agency;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataPackage that = (DataPackage) o;
        return Objects.equals(staticGTFSSource, that.staticGTFSSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(staticGTFSSource);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DataPackage.class.getSimpleName() + "[", "]")
                .add("staticGTFSSource=" + staticGTFSSource)
                .add("agencyMap=" + agencyMap)
                .add("stopMap=" + stopMap)
                .add("routeMap=" + routeMap)
                .add("tripMap=" + tripMap)
                .toString();
    }
}
