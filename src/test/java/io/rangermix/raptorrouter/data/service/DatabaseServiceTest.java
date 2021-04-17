package io.rangermix.raptorrouter.data.service;

import io.rangermix.raptorrouter.data.DataManager;
import io.rangermix.raptorrouter.routing.model.DataPackage;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration
class DatabaseServiceTest {

    @Setter(onMethod = @__({@Autowired}))
    DatabaseService databaseService;

    @Test
    void testWriteDataPackage() {
        DataPackage dataPackage = DataManager.getDataPackage(DataManager.getSydneyGtfsDao(DataManager.createInmemoryStore()));
        databaseService.saveDataPackageToDatabase(dataPackage);
    }

    @Test
    void testReadDataPackage() {
        DataPackage dataPackage = databaseService.getDataPackage();
        assertTrue(dataPackage.getAgencyMap().size()>1);
    }
}
