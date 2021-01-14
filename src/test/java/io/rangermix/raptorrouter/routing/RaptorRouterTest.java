package io.rangermix.raptorrouter.routing;

import io.rangermix.raptorrouter.DataManager;
import io.rangermix.raptorrouter.routing.model.DataPackage;
import io.rangermix.raptorrouter.routing.model.Stop;
import io.rangermix.raptorrouter.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
class RaptorRouterTest {

    DataPackage dataPackage;

    @BeforeEach
    void setUp() {
        dataPackage = DataManager.getDataPackage(DataManager.getSydneyGtfsDao(DataManager.getInmemoryStore()));
    }

    @Test
    void testRoute() {
        RaptorRouter raptorRouter = new RaptorRouter(dataPackage);
        Stop from = dataPackage.getStopMap()
                .get("PST1206"); // "PST1206","","Strathfield Station","-33.8716720139339","151.094285645918","1","","0",""
        Stop to = dataPackage.getStopMap()
                .get("PST1100"); // "PST1100","","Central Station","-33.8840842535493","151.206292455081","1","","0",""
        long startTime = LocalDateTime.parse("2020-12-05T10:15:30")
                .atZone(ZoneId.of("Australia/Sydney"))
                .toEpochSecond();
        Query query = new Query(dataPackage, from, to, startTime);

        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("start routing with query " + query);
        Itinerary itinerary = raptorRouter.route(query);
        stopWatch.lap("end routing");
    }
}
