package io.rangermix.raptorrouter;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.rangermix.raptorrouter.routing.model.Agency;
import io.rangermix.raptorrouter.routing.model.DataPackage;
import io.rangermix.raptorrouter.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
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

@Slf4j
public class DataManager {

    public static void main(String[] args) {
        var sydneyGtfsDao = getSydneyGtfsDao(getInmemoryStore());
        DataPackage dataPackage = getDataPackage(sydneyGtfsDao);
        MongoClient mongoClient = MongoClients.create(
                "mongodb+srv://rangermix:NgaW5ibcTnNkCKXG@sydney.krb3w.mongodb.net/model?retryWrites=true&w=majority");
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        MongoDatabase database = mongoClient.getDatabase("model").withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Agency> agencyMongoCollection = database.getCollection("agency", Agency.class);
        agencyMongoCollection.drop();

    }

    @NotNull
    public static DataPackage getDataPackage(GtfsMutableDao gtfsDao) {
        StopWatch stopWatch = new StopWatch(log);
        stopWatch.start("loading GTFS data into data package...");
        DataPackage dataPackage = new DataPackage(gtfsDao);
        stopWatch.lap("finished loading");
        return dataPackage;
    }

    @NotNull
    public static GtfsMutableDao getSydneyGtfsDao(@NotNull GtfsDaoImpl store) {
        var pathname = "full_greater_sydney_gtfs_static.zip";
        return readGtfsDataFromFile(pathname, store);
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
    public static GtfsDaoImpl getInmemoryStore() {
        return new GtfsDaoImpl();
    }

    public static GtfsMutableRelationalDao getDatabaseStore() {
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sessionFactory = new MetadataSources(ssr).buildMetadata().buildSessionFactory();
        HibernateGtfsFactory hibernateGtfsFactory = new HibernateGtfsFactory(sessionFactory);
        return hibernateGtfsFactory.getDao();
    }


}
