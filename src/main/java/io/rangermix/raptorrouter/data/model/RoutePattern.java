package io.rangermix.raptorrouter.data.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Data
@Builder
@Document
public class RoutePattern {
    @MongoId
    private String id;
    private String routeId;
    private List<String> stopIds;
    private List<String> tripIds;
    private String sampleTripId;
}
