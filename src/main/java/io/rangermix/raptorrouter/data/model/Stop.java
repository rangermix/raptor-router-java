package io.rangermix.raptorrouter.data.model;

import io.rangermix.raptorrouter.data.model.enums.BoardingInstruction;
import io.rangermix.raptorrouter.data.model.enums.LocationType;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Builder
@Document
public class Stop {
    @MongoId
    private String id;
    private String agencyId;
    private String name;
    private String code;
    private String desc;
    private String zoneId;
    private String url;
    private LocationType locationType;
    private String parentStationId;
    private BoardingInstruction wheelchairBoarding;
    private String direction;
    private String timezone;
    private int vehicleType;
    private String platformCode;
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint point;
    private List<String> transferIds;
    private List<String> routePatternIds;
}
