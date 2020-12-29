package io.rangermix;

import io.rangermix.routing.model.DataPackage;
import io.rangermix.util.StopWatch;
import org.jetbrains.annotations.NotNull;
import org.nustaq.serialization.FSTObjectOutput;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataSerializer {

    private static final Logger log = LoggerFactory.getLogger(DataSerializer.class);

    public static void buildSydney() throws IOException {
        GtfsDaoImpl store = getSydneyGtfsDao();
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("loading GTFS data into data package...");
        DataPackage dataPackage = new DataPackage(store);
        stopWatch.lap("finished loading");
        try (FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream("sydneyGTFS.dataPack"))) {
            out.getConf().setShareReferences(true);
            out.writeObject(dataPackage);
            stopWatch.lap("Serialized data is saved in sydneyGTFS.dataPack");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.currentTimeMillis();
    }

    @NotNull
    public static GtfsDaoImpl getSydneyGtfsDao() throws IOException {
        var reader = new GtfsReader();
        reader.setInputLocation(new File("full_greater_sydney_gtfs_static.zip"));
        var store = new GtfsDaoImpl();
        reader.setEntityStore(store);
        reader.run();
        log.info("Got:\n{}", store);
        return store;
    }
}
