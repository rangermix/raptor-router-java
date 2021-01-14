package io.rangermix.raptorrouter.routing.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsReader;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

class DataPackageTest {

    static GtfsDaoImpl store;

    @BeforeAll
    static void loadGTFS() throws IOException {
        GtfsReader reader = new GtfsReader();
        reader.setInputLocation(new File(Objects.requireNonNull(DataPackageTest.class.getClassLoader()
                .getResource("sample-feed.zip")).getPath()));
        store = new GtfsDaoImpl();
        reader.setEntityStore(store);
        reader.run();
    }

    @Test
    void testDataPackageConstruction() {
        var dataPackage = new DataPackage(store);
        System.out.println(dataPackage);
    }
}
