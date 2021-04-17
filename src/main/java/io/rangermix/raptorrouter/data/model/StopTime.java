package io.rangermix.raptorrouter.data.model;

import io.rangermix.raptorrouter.data.model.enums.Accuracy;
import io.rangermix.raptorrouter.data.model.enums.BoardingInstruction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class StopTime {
    @MongoId
    private String id;
    private String tripId;
    private long arrivalTime;
    private long departureTime;
    private String stopId;
    private int stopSequence;
    private String stopHeadsign;
    private BoardingInstruction pickupType;
    private BoardingInstruction dropOffType;
    private BoardingInstruction continuousPickup;
    private BoardingInstruction continuousDropOff;
    private double shapeDistTraveled;
//    private Accuracy farePeriodId;
}
