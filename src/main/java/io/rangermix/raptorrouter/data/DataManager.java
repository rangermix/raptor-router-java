package io.rangermix.raptorrouter.data;

import io.rangermix.raptorrouter.routing.model.DataPackage;
import io.rangermix.raptorrouter.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jetbrains.annotations.NotNull;
import org.onebusaway.gtfs.impl.GtfsDaoImpl;
import org.onebusaway.gtfs.serialization.GtfsReader;
import org.onebusaway.gtfs.services.GtfsMutableDao;
import org.onebusaway.gtfs.services.GtfsMutableRelationalDao;
import org.onebusaway.gtfs.services.HibernateGtfsFactory;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;

@Slf4j
public class DataManager {

    @NotNull
    public static GtfsDaoImpl createInmemoryStore() {
        return new GtfsDaoImpl();
    }

    public static GtfsMutableRelationalDao getDatabaseStore() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(ssr).buildMetadata().buildSessionFactory();
        HibernateGtfsFactory hibernateGtfsFactory = new HibernateGtfsFactory(sessionFactory);
        return hibernateGtfsFactory.getDao();
    }

    @NotNull
    private static GtfsMutableDao readGtfsDataFromFile(String pathname, GtfsMutableDao store) {
        var reader = new GtfsReader();
        try {
            reader.setInputLocation(new File(pathname));
            reader.setEntityStore(store);
            reader.run();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("Got GtfsDaoImpl with {} agencies and {} stops",
                store.getAllAgencies().size(),
                store.getAllStops().size());
        return store;
    }

    @NotNull
    public static GtfsMutableDao getSydneyGtfsDao(@NotNull GtfsMutableDao store) {
        var pathname = "full_greater_sydney_gtfs_static.zip";
        return readGtfsDataFromFile(pathname, store);
    }

    @NotNull
    public static DataPackage getDataPackage(@NotNull GtfsMutableDao gtfsDao) {
        assert !gtfsDao.getAllAgencies().isEmpty();
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("loading GTFS data into data package...");
        DataPackage dataPackage = new DataPackage(gtfsDao);
        stopWatch.lap("finished loading");
        return dataPackage;
    }

    public static void storeToMongoDb() {

    }


}
