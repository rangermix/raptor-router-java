package io.rangermix;

import io.rangermix.routing.model.DataPackage;
import io.rangermix.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsReader;

import java.io.File;
import java.io.IOException;

@Slf4j
public class DataManager {

    @NotNull
    public static DataPackage getDataPackage(GtfsDaoImpl gtfsDao) {
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("loading GTFS data into data package...");
        DataPackage dataPackage = new DataPackage(gtfsDao);
        stopWatch.lap("finished loading");
        return dataPackage;
    }

    @NotNull
    public static GtfsDaoImpl getSydneyGtfsDao() {
        var pathname = "full_greater_sydney_gtfs_static.zip";
        return getGtfsDao(pathname);
    }

    @NotNull
    private static GtfsDaoImpl getGtfsDao(String pathname) {
        var reader = new GtfsReader();
        var store = new GtfsDaoImpl();
        try {
            reader.setInputLocation(new File(pathname));
            reader.setEntityStore(store);
            reader.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Got GtfsDaoImpl with {} agencies and {} stops",
                store.getAllAgencies().size(),
                store.getAllStops().size());
        return store;
    }
}
