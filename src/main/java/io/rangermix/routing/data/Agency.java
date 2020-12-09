package io.rangermix.routing.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.TimeZone;

@Data
@AllArgsConstructor
public class Agency {
    String id;
    @NotNull String name;
    @NotNull String url;
    @NotNull TimeZone timeZone;
}
