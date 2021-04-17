package io.rangermix.raptorrouter.data.model;

import io.rangermix.raptorrouter.data.model.enums.Possibility;
import io.rangermix.raptorrouter.data.model.enums.RouteType;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Builder
@Document
@CompoundIndex(name = "agency_id", def = "{'agencyId': 1, 'id': 1}")
public class Route {
    @MongoId
    private String id;
    private String agencyId;
    private String shortName;
    private String longName;
    private RouteType type;
    private String desc;
    private String url;
    private String color;
    private String textColor;
    private Possibility bikesAllowed;
    private int sortOrder;
    private List<String> routePatternIds;
}
