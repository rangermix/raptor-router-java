package io.rangermix.routing.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.TimeZone;

@Data
@RequiredArgsConstructor
public class Agency implements Serializable {
    @Serial
    private static final long serialVersionUID = -228914687065757433L;
    final String id;
    final String name;
    final String url;
    final TimeZone timeZone;
}
