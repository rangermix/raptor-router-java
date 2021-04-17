package io.rangermix.raptorrouter.data.model;

import io.rangermix.raptorrouter.data.model.enums.Possibility;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

/**
 * GTFS Trip
 * Reference: <a href="https://developers.google.com/transit/gtfs/reference#tripstxt">https://developers.google.com/transit/gtfs/reference#tripstxt</a>
 */
@Data
@Builder
@Document
public class Trip {
    @MongoId
    private String id;
    private String routeId;
//    private String routePatternId;
    private String serviceId;
    private String tripHeadsign;
    private String tripShortName;
    private String directionId;
    private String blockId;
    private String shapeId;
    private Possibility wheelchairAccessible;
    private Possibility bikesAllowed;
    private List<String> stopTimeIds;
}
