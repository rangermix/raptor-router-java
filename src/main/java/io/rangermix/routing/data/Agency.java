package io.rangermix.routing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.TimeZone;

@Data
@AllArgsConstructor
public class Agency implements Serializable {
    @Serial
    private static final long serialVersionUID = -228914687065757433L;
    String id;
    @NotNull String name;
    @NotNull String url;
    @NotNull TimeZone timeZone;
}
