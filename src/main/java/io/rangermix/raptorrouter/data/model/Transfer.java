package io.rangermix.raptorrouter.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Builder
@Document
public class Transfer {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String stopAId;
    private String stopBId;
    private double distance;
    private long time;
}
