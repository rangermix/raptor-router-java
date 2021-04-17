package io.rangermix.raptorrouter.data.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@Document
public class Agency {
    @MongoId
    private String id;
    private String name;
    private String url;
    private String timeZone;
    private String lang;
    private String phone;
    private String fareUrl;
    private String email;
}
