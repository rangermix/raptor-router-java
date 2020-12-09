import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    public static void main(String[] args) throws IOException {
        GtfsReader reader = new GtfsReader();
        reader.setInputLocation(new File(Objects.requireNonNull(DataLoader.class.getClassLoader().getResource("sample-feed.zip")).getPath()));
        GtfsDaoImpl store = new GtfsDaoImpl();
        reader.setEntityStore(store);
        reader.run();
        log.info("Got:\n{}", store.getAllRoutes());
        System.currentTimeMillis();
    }
}
