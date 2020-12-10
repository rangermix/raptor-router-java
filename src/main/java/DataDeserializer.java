import io.rangermix.routing.data.DataPackage;
import io.rangermix.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.nustaq.serialization.FSTObjectInput;

import java.io.FileInputStream;

@Slf4j
public class DataDeserializer {

    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("loading data package from sydneyGTFS.dataPack...");
        DataPackage dataPackage = null;
        try (FSTObjectInput in = new FSTObjectInput(new FileInputStream("sydneyGTFS.dataPack"))) {
            dataPackage = (DataPackage) in.readObject();
            stopWatch.lap("Serialized data read from sydneyGTFS.dataPack");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dataPackage.equals(null)) {
            System.currentTimeMillis();
        } else {
            System.currentTimeMillis();
        }
        System.currentTimeMillis();
    }
}
