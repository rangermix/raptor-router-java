package io.rangermix.raptorrouter.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@Builder
@Document
public class Service {
    @MongoId
    private String id;
    private byte[] dayMask;
    private ZoneId zoneId;
    private Instant startDate;
    private Instant endDate;
    private int numDays;
}
