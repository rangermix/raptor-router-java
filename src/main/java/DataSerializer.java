import io.rangermix.routing.data.DataPackage;
import io.rangermix.util.StopWatch;
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

    public static void main(String[] args) throws IOException {
        GtfsReader reader = new GtfsReader();
        var gtfsZip = DataSerializer.class.getClassLoader().getResource("full_greater_sydney_gtfs_static.zip");
        reader.setInputLocation(new File(gtfsZip.getPath()));
        GtfsDaoImpl store = new GtfsDaoImpl();
        reader.setEntityStore(store);
        reader.run();
        log.info("Got:\n{}", store);
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("loading GTFS data into data package...");
        DataPackage dataPackage = new DataPackage(store);
        stopWatch.lap("finished loading");
        try (FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream("sydneyGTFS.dataPack"))) {
            out.writeObject(dataPackage);
            stopWatch.lap("Serialized data is saved in sydneyGTFS.dataPack");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.currentTimeMillis();
    }
}
