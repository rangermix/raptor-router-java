package io.rangermix.raptorrouter.data;

import io.rangermix.raptorrouter.routing.model.DataPackage;
import io.rangermix.raptorrouter.util.StopWatch;
import org.nustaq.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;

public class DataSerializer {

    private static final Logger log = LoggerFactory.getLogger(DataSerializer.class);

    public static void buildSydney() throws IOException {
        StopWatch stopWatch = new StopWatch(log);
        DataPackage dataPackage = DataManager.getDataPackage(DataManager.getSydneyGtfsDao(DataManager.createInmemoryStore()));
        try (FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream("sydneyGTFS.dataPack"))) {
            out.getConf().setShareReferences(true);
            out.writeObject(dataPackage);
            stopWatch.lap("Serialized data is saved in sydneyGTFS.dataPack");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
